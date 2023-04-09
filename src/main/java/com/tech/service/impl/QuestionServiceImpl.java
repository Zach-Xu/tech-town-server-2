package com.tech.service.impl;

import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.tech.exception.AuthException;
import com.tech.exception.NotFoundException;
import com.tech.model.*;
import com.tech.repo.UserRepository;
import com.tech.repo.VoteRepository;
import com.tech.service.ActivityService;
import com.tech.utils.ActionType;
import com.tech.vo.QuestionResponse;
import com.tech.vo.ResponseResult;
import com.tech.repo.QuestionRepository;
import com.tech.service.QuestionService;
import com.tech.vo.UserResponse;
import org.apache.logging.log4j.util.TriConsumer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.BiConsumer;

import static com.tech.utils.Constants.*;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityService activityService;

    @Override
    public ResponseResult<List<QuestionResponse>> getAllQuestions() {
        // Fetches the first 20 questions
        Page<Question> questionPage = questionRepository.findAll(PageRequest.ofSize(20));

        // Extracts the questions from the Page object
        List<Question> questions = questionPage.getContent();

        // convert list of questions to list of questionResponse
        List<QuestionResponse> questionResponseList = convertToQuestionResponseList(questions);

        return new ResponseResult<>(HTTPResponse.SC_OK, "retrieved first 100 questions", questionResponseList);
    }

    @Override
    public ResponseResult<Question> createQuestion(Question question) {
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (Objects.isNull(loginUser)) {
            throw new AuthException("invalid user");
        }

        // loginUser is in detached stage but this won't throw an error
        // because many-to-one relationship will not persist user again
        question.setUser(loginUser);
        question.getTags().forEach(tag -> tag.setQuestion(question));
        questionRepository.save(question);

        // create corresponding activity
        Activity activity = new Activity();
        // assign values to fields
        activity.setAction(ActionType.QUESTION);
        activity.setUserId(loginUser.getId());
        activity.setQuestion(question);
        activityService.createActivity(activity);

        return new ResponseResult<>(HTTPResponse.SC_CREATED, "created question successfully", question);
    }

    @Override
    public ResponseResult<Map<String,Object>> getQuestion(Long questionId) {
        Optional<Question> question = questionRepository.findById(questionId);
        Question updatedQuestion = question.map(q -> {
                    // increase the number of views by 1
                    q.setViews(q.getViews() + 1);
                    // update the question
                    questionRepository.save(q);
                    // return updated question
                    return q;
                })
                .orElseThrow(() -> new NotFoundException("Question with id " + questionId + " not found"));

        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        Vote vote = voteRepository.findByQuestionIdAndUserId(questionId, loginUser.getId())
                .orElseGet(Vote::new);

        Map questionMap = new HashMap();
        questionMap.put("question", updatedQuestion);
        questionMap.put("userVoteStatus", vote.getStatus());
        questionMap.put("isUserBookmarked", questionRepository.existsByIdAndBookmarkedUsersIn(questionId, Arrays.asList(loginUser)));

        return new ResponseResult<>(HTTPResponse.SC_OK, "fetched question with id " + questionId + " successfully", questionMap);

    }

    @Transactional
    @Override
    public ResponseResult<Vote> voteQuestion(Long questionId, Vote vote) {
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (Objects.isNull(loginUser)) {
            throw new AuthException("invalid user");
        }

        Optional<Vote> voteDB = voteRepository.findByQuestionIdAndUserId(questionId, loginUser.getId());
        int currentVoteStatus = vote.getStatus();
        voteDB.ifPresentOrElse(
                // update previous record
                v -> {
                    int previousVoteStatus = v.getStatus();
                    if (previousVoteStatus == currentVoteStatus) {
                        // do nothing on same vote actions
                        return;
                    }

                    // update vote count in question
                    Optional<Question> votedQuestion = questionRepository.findById(questionId);
                    votedQuestion.map(question -> {
                                // update the vote status on question based on current and previous vote status
                                Map<Integer, TriConsumer<Question, Integer, Long>> voteActions = new HashMap<>();
                                voteActions.put(CANCEL_VOTE, this::cancelVote);
                                voteActions.put(UP_VOTE, this::upVote);
                                voteActions.put(DOWN_VOTE, this::downVote);
                                voteActions.get(currentVoteStatus).accept(question, previousVoteStatus, loginUser.getId());

                                // update vote status to current
                                v.setStatus(currentVoteStatus);
                                voteRepository.save(v);

                                return questionRepository.save(question);
                            })
                            .orElseThrow(() -> new NotFoundException("Question with id " + questionId + " not found"));
                }, () -> {
                    // create a new vote record
                    Optional<Question> questionOnVote = questionRepository.findById(questionId);
                    questionOnVote.map(question -> {
                                // no need to cancel previous vote status on question as it's the first time to vote
                                switch (currentVoteStatus) {
                                    case UP_VOTE -> {
                                        question.setUpVotes(question.getUpVotes() + 1);
                                        // create up vote activity
                                        Activity activity = new Activity();
                                        activity.setQuestion(question);
                                        activity.setUserId(loginUser.getId());
                                        activity.setAction(ActionType.VOTE);
                                        activityService.createActivity(activity);
                                    }
                                    case DOWN_VOTE -> question.setDownVotes(question.getDownVotes() + 1);
                                    default -> throw new IllegalArgumentException("Invalid vote status");
                                }
                                // set relationship with other entities
                                vote.setUser(loginUser);
                                vote.setQuestion(question);
                                // create vote in database
                                voteRepository.save(vote);

                                return questionRepository.save(question);
                            })
                            .orElseThrow(() -> new NotFoundException("Question with id " + questionId + " not found"));
                }
        );

        return voteDB.map(v -> new ResponseResult<>(200, "voted question successfully", v))
                .orElse(new ResponseResult<>(200, "voted question successfully", vote));
    }

    @Override
    public void cancelVote(Question question, int previousVoteStatus, Long userId) {
        // current action is cancel, no need to modify votes count in question

        // remove vote activity
        activityService.deleteVoteActivity(question.getId(), userId);
        this.cancelPreviousVoteAction(question, previousVoteStatus);
    }

    @Override
    public void upVote(Question question, int previousVoteStatus, Long userId) {
        // perform current up vote action
        question.setUpVotes(question.getUpVotes() + 1);

        // create up vote activity
        Activity activity = new Activity();
        activity.setQuestion(question);
        activity.setUserId(userId);
        activity.setAction(ActionType.VOTE);
        activityService.createActivity(activity);
        this.cancelPreviousVoteAction(question, previousVoteStatus);
    }

    @Override
    public void downVote(Question question, int previousVoteStatus, Long userId) {
        // remove vote activity
        activityService.deleteVoteActivity(question.getId(), userId);

        // perform current down vote action
        question.setDownVotes(question.getDownVotes() + 1);
        this.cancelPreviousVoteAction(question, previousVoteStatus);
    }

    @Override
    public void cancelPreviousVoteAction(Question question, int previousVoteStatus) {
        switch (previousVoteStatus) {
            case CANCEL_VOTE:
                break;
            case UP_VOTE:
                // cancel up vote
                question.setUpVotes(question.getUpVotes() - 1);
                break;
            case DOWN_VOTE:
                // cancel down vote
                question.setDownVotes(question.getDownVotes() - 1);
                break;
        }
    }

    @Override
    public ResponseResult<Vote> getUserVoteOnQuestion(Long questionId) {
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Vote> userVoteOnQuestion = voteRepository.findByQuestionIdAndUserId(questionId, loginUser.getId());
        return userVoteOnQuestion.map(vote -> new ResponseResult(HTTPResponse.SC_OK, "fetched vote info successfully", vote))
                .orElseThrow(() -> new NotFoundException("Vote not found"));
    }

    @Override
    public ResponseResult<List<QuestionResponse>> getUserBookmarkedQuestions(Long userId) {
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user;
        if (Objects.isNull(userId)) {
            user = userRepository.findById(loginUser.getId()).orElseThrow(() -> new NotFoundException("User: " + userId + " not found"));
        } else {
            user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User: " + userId + " not found"));
        }

        // convert list of questions to list of questionResponse
        List<QuestionResponse> questionResponseList = convertToQuestionResponseList(user.getBookmarks());
        return new ResponseResult<>(HTTPResponse.SC_OK, "Fetched bookmark list successfully", questionResponseList);
    }

    @Override
    public ResponseResult bookmarkQuestion(Long questionId) {
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(loginUser.getId()).orElseThrow(() -> new NotFoundException("Receiver not found"));

        Question question = questionRepository.findById(questionId).orElseThrow(() -> new NotFoundException("Question not fo" +
                "und: " + questionId));
        question.getBookmarkedUsers().add(user);
        user.getBookmarks().add(question);
        questionRepository.save(question);
        return new ResponseResult(HTTPResponse.SC_OK, "Bookmarked question :" + questionId + " successfully");
    }

    @Override
    public ResponseResult unBookmarkQuestion(Long questionId) {
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(loginUser.getId()).orElseThrow(() -> new NotFoundException("Receiver not found"));

        Question question = questionRepository.findById(questionId).orElseThrow(() -> new NotFoundException("Question not found: " + questionId));

        question.getBookmarkedUsers().removeIf(u -> u.getId().equals(user.getId()));

        user.getBookmarks().removeIf(q -> q.getId().equals(questionId));

        questionRepository.save(question);
        return new ResponseResult(HTTPResponse.SC_OK, "unBookmarked question :" + questionId + " successfully");
    }

    @Override
    public List<QuestionResponse> convertToQuestionResponseList(List<Question> questions) {
        List<QuestionResponse> questionResponseList = new ArrayList<>();

        questions.forEach(question -> {
            QuestionResponse questionResponse = new QuestionResponse();
            UserResponse userResponse = new UserResponse();

            // Copies the properties from the entities to create custom view objects
            BeanUtils.copyProperties(question, questionResponse);
            BeanUtils.copyProperties(question.getUser(), userResponse);
            questionResponse.setUser(userResponse);

            // calculate the votes
            questionResponse.setVotes(question.getUpVotes() - question.getDownVotes());

            // set the number of answers instead of returning the entire answer object
            questionResponse.setNumOfAnswers(question.getAnswers().size());

            // Add converted question view objects to the list
            questionResponseList.add(questionResponse);
        });

        return questionResponseList;
    }

}
