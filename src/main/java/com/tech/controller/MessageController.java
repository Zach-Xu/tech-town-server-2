package com.tech.controller;

import com.tech.dto.MessageDTO;
import com.tech.service.MessageService;
import com.tech.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/inbox")
public class MessageController {

    @Autowired
    MessageService messageService;

    @PostMapping("/message")
    public ResponseResult createMessage(@RequestBody MessageDTO messageDTO){
        return messageService.createMessage(messageDTO);
    }

}
