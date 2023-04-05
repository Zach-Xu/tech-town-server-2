package com.tech.service;

import com.tech.vo.ProfileResponse;
import com.tech.vo.ResponseResult;
import com.tech.vo.UserCardResponse;

public interface ProfileService {

    ResponseResult<ProfileResponse> getUserProfile(Long userId);

    ResponseResult<UserCardResponse> getUserCardInfo(Long userId);

    ResponseResult getUserRepos(String username);
}
