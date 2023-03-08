package com.tech.service.impl;

import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.tech.dto.QuestionDTO;
import com.tech.exception.AuthException;
import com.tech.model.User;
import com.tech.vo.QuestionResponse;
import com.tech.vo.ResponseResult;
import com.tech.model.Question;
import com.tech.repo.QuestionRepository;
import com.tech.service.QuestionService;
import com.tech.vo.TagResponse;
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

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

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

            List<TagResponse> tagResponseList = new ArrayList<>();
            // Iterates over each tag object and converts it into a TagResponse object
            question.getTags().forEach(tag -> {
                TagResponse tagResponse = new TagResponse();
                BeanUtils.copyProperties(tag, tagResponse);
                tagResponseList.add(tagResponse);
            });

            questionResponse.setTags(tagResponseList);
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
}
