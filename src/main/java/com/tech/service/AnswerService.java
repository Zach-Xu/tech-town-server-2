package com.tech.service;

import com.tech.dto.AnswerDTO;
import com.tech.model.Answer;
import com.tech.vo.ResponseResult;

public interface AnswerService {

    ResponseResult<Answer> createAnswer(Long questionId, AnswerDTO answer);

}
