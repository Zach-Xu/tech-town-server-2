package com.tech.service.impl;

import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.tech.exception.NotFoundException;
import com.tech.model.Profile;
import com.tech.model.User;
import com.tech.repo.*;
import com.tech.service.ProfileService;
import com.tech.utils.FollowStatus;
import com.tech.vo.ProfileResponse;
import com.tech.vo.ResponseResult;
import com.tech.vo.UserCardResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Override
    public ResponseResult<ProfileResponse> getUserProfile(Long userId) {
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found: " + userId));

        // get user profile, create one if not present in database
        Profile profile = profileRepository.findByUserId(userId).orElseGet(() -> {
                    Profile p = new Profile(userId);
                    profileRepository.save(p);
                    return p;
                }
        );


        ProfileResponse profileResponse = new ProfileResponse();
        BeanUtils.copyProperties(profile, profileResponse);

        profileResponse.setJoinTime(user.getCreatedTime());
        profileResponse.setQuestions(questionRepository.countByUser_Id(userId).intValue());
        profileResponse.setUsername(user.getUsername());
        profileResponse.setAnswers(answerRepository.countByUser_Id(userId).intValue());
        profileResponse.setFollowers(followRepository.countByUserIdAndStatus(userId, FollowStatus.FOLLOW).intValue());
        profileResponse.setFollowing(followRepository.countByFollowerIdAndStatus(userId, FollowStatus.FOLLOW).intValue());

        // fetch profile for loggedIn user
        if(loginUser.getId().equals(userId)){
            profileResponse.setFollowed(false);
        }else {
            profileResponse.setFollowed(followRepository.existsByUserIdAndFollowerIdAndStatus(userId, loginUser.getId(), FollowStatus.FOLLOW));
        }
        return new ResponseResult<>(HTTPResponse.SC_OK, "fetched user profile successfully", profileResponse);
    }

    @Override
    public ResponseResult<UserCardResponse> getUserCardInfo(Long userId) {
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found: " + userId));

        UserCardResponse userCardResponse = new UserCardResponse();
        userCardResponse.setFollowing(followRepository.countByFollowerIdAndStatus(userId, FollowStatus.FOLLOW).intValue());
        userCardResponse.setFollowers(followRepository.countByUserIdAndStatus(userId, FollowStatus.FOLLOW).intValue());
        userCardResponse.setQuestions(questionRepository.countByUser_Id(userId).intValue());
        userCardResponse.setAnswers(answerRepository.countByUser_Id(userId).intValue());

        // fetch user card for loggedIn user
        if(loginUser.getId().equals(userId)){
            userCardResponse.setFollowed(false);
        }else {
            userCardResponse.setFollowed(followRepository.existsByUserIdAndFollowerIdAndStatus(userId, loginUser.getId(), FollowStatus.FOLLOW));
        }

        return new ResponseResult<>(HTTPResponse.SC_OK, "fetched user card successfully", userCardResponse);
    }
}
