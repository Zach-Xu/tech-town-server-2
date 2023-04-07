package com.tech.service;

import com.tech.dto.ProfileDTO;
import com.tech.model.Profile;
import com.tech.vo.ProfileResponse;
import com.tech.vo.ResponseResult;
import com.tech.vo.UserCardResponse;

public interface ProfileService {

    ResponseResult<ProfileResponse> getUserProfile(Long userId);

    ResponseResult<UserCardResponse> getUserCardInfo(Long userId);

    ResponseResult getUserRepos(String username);

    ResponseResult<Profile> updateUserProfile(ProfileDTO profile);
}
