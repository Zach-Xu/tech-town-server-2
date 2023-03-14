package com.tech.service.impl;

import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.tech.exception.AuthException;
import com.tech.exception.NotFoundException;
import com.tech.model.User;
import com.tech.model.Vote;
import com.tech.repo.VoteRepository;
import com.tech.vo.QuestionResponse;
import com.tech.vo.ResponseResult;
import com.tech.model.Question;
import com.tech.repo.QuestionRepository;
import com.tech.service.QuestionService;
import com.tech.vo.UserResponse;
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

    @Override
    public ResponseResult<List<QuestionResponse>> getAllQuestions() {
        // Fetches the first 20 questions
        Page<Question> questionPage = questionRepository.findAll(PageRequest.ofSize(20));

        // Extracts the questions from the Page object
        List<Question> questions = questionPage.getContent();

        List<QuestionResponse> questionResponseList = new ArrayList<>();

        // Iterates over each Question object and converts it into a QuestionResponse object
        questions.forEach(question -> {
            QuestionResponse questionResponse = new QuestionResponse();
            UserResponse userResponse = new UserResponse();

            // Copies the properties from the entities to custom view objects
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

        return new ResponseResult<>(HTTPResponse.SC_OK, "retrieved first 100 questions", questionResponseList);
    }

    @Override
    public ResponseResult<Question> createQuestion(Question question) {
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (Objects.isNull(loginUser)) {
            throw new AuthException("invalid user");
        }

        question.setUser(loginUser);
        question.getTags().forEach(tag -> tag.setQuestion(question));
        questionRepository.save(question);
        return new ResponseResult<>(HTTPResponse.SC_CREATED, "created question successfully", question);
    }

    @Override
    public ResponseResult<Question> getQuestion(Long questionId) {
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

        return new ResponseResult<>(HTTPResponse.SC_OK, "fetched question with id " + questionId + " successfully", updatedQuestion);

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
                                Map<Integer, BiConsumer<Question, Integer>> voteActions = new HashMap<>();
                                voteActions.put(CANCEL_VOTE, this::cancelVote);
                                voteActions.put(UP_VOTE, this::upVote);
                                voteActions.put(DOWN_VOTE, this::downVote);
                                voteActions.get(currentVoteStatus).accept(question, previousVoteStatus);

                                // update vote status to current
                                v.setStatus(currentVoteStatus);
                                voteRepository.save(v);

                                return questionRepository.save(question);
                            })
                            .orElseThrow(() -> new NotFoundException("Question with id " + questionId + " not found"));
                }, () -> {
                    // first time to vote a question
                    Optional<Question> votedQuestion = questionRepository.findById(questionId);
                    votedQuestion.map(question -> {
                                // no need to cancel previous vote status on question
                                switch (currentVoteStatus) {
                                    case UP_VOTE -> question.setUpVotes(question.getUpVotes() + 1);
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
    public void cancelVote(Question question, int previousVoteStatus) {
        // current action is cancel, no need to modify votes count in question

        this.cancelPreviousVoteAction(question, previousVoteStatus);
    }

    @Override
    public void upVote(Question question, int previousVoteStatus) {
        // perform current up vote action
        question.setUpVotes(question.getUpVotes() + 1);

        this.cancelPreviousVoteAction(question, previousVoteStatus);
    }

    @Override
    public void downVote(Question question, int previousVoteStatus) {
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
        if (Objects.isNull(loginUser)) {
            throw new AuthException("invalid user");
        }

        Optional<Vote> userVoteOnQuestion = voteRepository.findByQuestionIdAndUserId(questionId, loginUser.getId());
        return userVoteOnQuestion.map(vote -> new ResponseResult(HTTPResponse.SC_OK, "fetched vote info successfully", vote))
                .orElseThrow(() -> new NotFoundException("Vote not found"));
    }


}
