package com.tech.controller;

import com.tech.dto.InboxDTO;
import com.tech.dto.MessageDTO;
import com.tech.service.InboxService;
import com.tech.service.MessageService;
import com.tech.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/inbox")
public class InboxController {

    @Autowired
    MessageService messageService;

    @Autowired
    InboxService inboxService;

    @GetMapping("/all")
    public ResponseResult getInboxList(){
        return inboxService.getAllInboxes();
    }

    @PostMapping
    public ResponseResult createInbox(@RequestBody InboxDTO inboxDTO){
        return inboxService.createInbox(inboxDTO);
    }

    @PostMapping("/message")
    public ResponseResult createMessage(@RequestBody MessageDTO messageDTO){
        return messageService.createMessage(messageDTO);
    }

    @GetMapping("/{inboxId}/messages")
    public ResponseResult getAllInboxMessages(@PathVariable("inboxId")Long inboxId){
        return messageService.getAllMessagesByInboxId(inboxId);
    }

    @GetMapping("/{inboxId}")
    public ResponseResult getInbox(@PathVariable("inboxId")Long inboxId){
        return messageService.getInboxById(inboxId);
    }

}
