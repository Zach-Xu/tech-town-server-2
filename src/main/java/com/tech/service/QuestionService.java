package com.tech.service;

import com.tech.dto.QuestionDTO;
import com.tech.vo.QuestionResponse;
import com.tech.vo.ResponseResult;
import com.tech.model.Question;

import java.util.List;

public interface QuestionService {

    ResponseResult<List<QuestionResponse>> getAllQuestions();

    ResponseResult<Question> getQuestionById();

    ResponseResult<Question> createQuestion(Question question);
}
