package com.tech.service;

import com.tech.dto.PromptDTO;
import com.tech.vo.ResponseResult;

public interface ChatGPTService {

    ResponseResult createCompletion(PromptDTO promptDTO);
}
