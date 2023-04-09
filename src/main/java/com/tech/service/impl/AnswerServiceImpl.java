package com.tech.service.impl;

import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.tech.dto.AnswerDTO;
import com.tech.exception.NotFoundException;
import com.tech.model.Activity;
import com.tech.model.Answer;
import com.tech.model.Question;
import com.tech.model.User;
import com.tech.repo.AnswerRepository;
import com.tech.repo.QuestionRepository;
import com.tech.service.ActivityService;
import com.tech.service.AnswerService;
import com.tech.utils.ActionType;
import com.tech.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AnswerServiceImpl implements AnswerService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private ActivityService activityService;

    @Override
    public ResponseResult<Answer> createAnswer(Long questionId, AnswerDTO answer) {
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        Answer createdAnswer = optionalQuestion.map(q -> {
            if (loginUser.getId().equals(questionId)) {
                throw new IllegalArgumentException("You are not allowed to answer your own question!");
            }
            // create a new answer entity and set required fields
            Answer newAnswer = new Answer();
            newAnswer.setContent(answer.getContent());
            newAnswer.setUser(loginUser);
            newAnswer.setQuestion(q);
            // save new answer to database
            answerRepository.save(newAnswer);

            // create corresponding answer activity
            // create up vote activity
            Activity activity = new Activity();
            activity.setQuestion(q);
            activity.setUserId(loginUser.getId());
            activity.setAction(ActionType.ANSWER);
            activityService.createActivity(activity);

            return newAnswer;
        }).orElseThrow(() -> new NotFoundException(("Question with id " + questionId) + " not found"));

        return new ResponseResult(HTTPResponse.SC_CREATED, "Answered created successfully", createdAnswer);
    }
}
