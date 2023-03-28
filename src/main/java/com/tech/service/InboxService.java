package com.tech.service;

import com.tech.model.Inbox;
import com.tech.vo.ResponseResult;

import java.util.List;

public interface InboxService {

    ResponseResult<List<Inbox>> getAllInboxes();
}
