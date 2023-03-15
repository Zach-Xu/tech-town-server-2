package com.tech.controller;

import com.tech.dto.PromptDTO;
import com.tech.service.ChatGPTService;
import com.tech.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/chat")
public class ChatController {

    @Autowired
    ChatGPTService chatGPTService;

    @PostMapping("/chatgpt")
    public ResponseResult getChatGPTMessage(@RequestBody PromptDTO promptDTO){
        return chatGPTService.createCompletion(promptDTO);
    }
}
