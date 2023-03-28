package com.tech.service.impl;

import com.tech.exception.AuthException;
import com.tech.model.Inbox;
import com.tech.model.User;
import com.tech.repo.InboxRepository;
import com.tech.service.InboxService;
import com.tech.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

public class InboxServiceImpl implements InboxService {

    @Autowired
    private InboxRepository inboxRepository;

    @Override
    public ResponseResult<List<Inbox>> getAllInboxes() {
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (Objects.isNull(loginUser)) {
            throw new AuthException("invalid user");
        }
        List<User> participants = new ArrayList<>(Arrays.asList(loginUser));
        Optional<Inbox> byParticipantsIn = inboxRepository.findByParticipantsIn(participants);
        return null;
    }
}
