package com.tech.service.impl;

import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.tech.dto.InboxDTO;
import com.tech.exception.AuthException;
import com.tech.exception.NotFoundException;
import com.tech.model.Inbox;
import com.tech.model.Message;
import com.tech.model.User;
import com.tech.repo.InboxRepository;
import com.tech.repo.UserRepository;
import com.tech.service.InboxService;
import com.tech.utils.InboxType;
import com.tech.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InboxServiceImpl implements InboxService {

    @Autowired
    private InboxRepository inboxRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseResult<List<Inbox>> getAllInboxes() {
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<User> participants = new ArrayList<>(Arrays.asList(loginUser));
        Optional<List<Inbox>> inboxes = inboxRepository.findAllByParticipantsIn(participants);

        return new ResponseResult<>(HTTPResponse.SC_OK, "fetched inbox list successfully", inboxes.orElse(new ArrayList<>()));
    }

    @Override
    public ResponseResult<Inbox> createInbox(InboxDTO inboxDTO) {
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // fetch receiver from database
        User sender = userRepository.findById(loginUser.getId()).orElseThrow(() -> new NotFoundException("Receiver not found"));
        User receiver = userRepository.findById(inboxDTO.getUserId()).orElseThrow(() -> new NotFoundException("Receiver not found"));

        // check if an inbox already exists for this sender-receiver pair
        List<User> participants = new ArrayList<>(Arrays.asList(sender, receiver));
        List<Message> messages = new ArrayList<>();
        InboxType type = inboxDTO.getType();
        inboxRepository.findByParticipants(new ArrayList<>(Arrays.asList(sender.getId(), receiver.getId())))
                .ifPresent(inbox -> {
                    throw new IllegalArgumentException("Inbox already created: " + inbox.getId());
                });

        Inbox newInbox = new Inbox();
        newInbox.setParticipants(participants);
        participants.forEach(p -> p.getInboxes().add(newInbox));
        newInbox.setMessages(messages);
        newInbox.setType(type);
        inboxRepository.save(newInbox);
        return new ResponseResult<>(HTTPResponse.SC_CREATED, "Inbox created successfully", newInbox);
    }


}
