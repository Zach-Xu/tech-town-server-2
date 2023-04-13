package com.tech.service;

import com.tech.vo.ResponseResult;

public interface UserService {
    ResponseResult getUsersByKeyword(String keyword);
}
