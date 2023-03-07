package com.tech.service.impl;

import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.tech.dto.QuestionDTO;
import com.tech.exception.AuthException;
import com.tech.model.User;
import com.tech.vo.ResponseResult;
import com.tech.model.Question;
import com.tech.repo.QuestionRepository;
import com.tech.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public ResponseResult<List<Question>> getAllQuestions() {
        Page<Question> questionPage = questionRepository.findAll(PageRequest.ofSize(100));
        List<Question> questions = questionPage.getContent();

        return new ResponseResult(HTTPResponse.SC_OK, "retrieved first 100 questions", questions);
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
