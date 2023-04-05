package com.tech.controller;

import com.tech.service.ProfileService;
import com.tech.vo.ProfileResponse;
import com.tech.vo.ResponseResult;
import com.tech.vo.UserCardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/profile")
public class ProfileController{

    @Autowired
    ProfileService profileService;

    @GetMapping("/user/{userId}")
    ResponseResult<ProfileResponse> getUserProfile(@PathVariable("userId") Long userId){
        return profileService.getUserProfile(userId);
    }

    @GetMapping("/user-card/{userId}")
    ResponseResult<UserCardResponse> getUserCardInfo(@PathVariable("userId") Long userId){
        return profileService.getUserCardInfo(userId);
    }

    @GetMapping("/github/{username}")
    ResponseResult getUserRepos(@PathVariable("username") String username){
        return profileService.getUserRepos(username);
    }
}
