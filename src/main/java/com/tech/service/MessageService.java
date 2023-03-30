package com.tech.service;

import com.tech.dto.MessageDTO;
import com.tech.model.Message;
import com.tech.vo.ResponseResult;
import org.springframework.transaction.annotation.Transactional;

public interface MessageService {

    @Transactional
    ResponseResult createMessage(MessageDTO messageDTO);

    ResponseResult getAllMessagesByInboxId(Long inboxId);
}
