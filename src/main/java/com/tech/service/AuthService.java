package com.tech.service;

import com.tech.model.User;
import com.tech.dto.ResponseResult;

public interface AuthService {

    ResponseResult login(User user);

    ResponseResult register(User user);

    ResponseResult logout();

}
