package com.tech.service.impl;

import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.tech.dto.AnswerDTO;
import com.tech.exception.AuthException;
import com.tech.exception.NotFoundException;
import com.tech.model.Answer;
import com.tech.model.User;
import com.tech.repo.AnswerRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

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

            // set the number of answers instead of returning the entire answer object
            questionResponse.setNumOfAnswers(question.getAnswers().size());

            // Add converted question view objects to the list
            questionResponseList.add(questionResponse);
        });

        return new ResponseResult(HTTPResponse.SC_OK, "retrieved first 100 questions", questionResponseList);
    }

    @Override
    public ResponseResult<Question> getQuestionById() {
        return null;
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
        return new ResponseResult(HTTPResponse.SC_CREATED, "created question successfully", question);
    }

    @Override
    public ResponseResult<Question> getQuestion(Long questionId) {
        Optional<Question> question = questionRepository.findById(questionId);
        return question.map(q -> new ResponseResult(HTTPResponse.SC_OK, "fetched question with id " + questionId + " successfully", q))
                .orElseThrow(() -> new NotFoundException("Question with id " + questionId + " not found"));

    }

    @Override
    public ResponseResult<Question> createAnswer(Long questionId, AnswerDTO answer) {
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (Objects.isNull(loginUser)) {
            throw new AuthException("invalid user");
        }

        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        Question question = optionalQuestion.map(q -> {
            if(loginUser.getId().equals(questionId)) {
                throw new IllegalArgumentException("You are not allowed to answer your own question!");
            }
            List<Answer> answers = q.getAnswers();
            // create a new answer entity and set required fields
            Answer newAnswer = new Answer();
            newAnswer.setContent(answer.getContent());
            newAnswer.setUser(loginUser);
            newAnswer.setQuestion(q);
            // save new answer to database
            answerRepository.save(newAnswer);
            // add to the answers list of the question
            answers.add(newAnswer);
            return q;
        }).orElseThrow(() -> new NotFoundException(("Question with id " + questionId) + " not found"));

        // update question and save new answer to database
//        questionRepository.save(question);
        return new ResponseResult(HTTPResponse.SC_CREATED, "Answered created successfully", question);
    }
}
