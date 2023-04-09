package com.tech.service.impl;

import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.tech.model.Activity;
import com.tech.model.User;
import com.tech.repo.ActivityRepository;
import com.tech.repo.QuestionRepository;
import com.tech.service.ActivityService;
import com.tech.utils.ActionType;
import com.tech.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public ResponseResult<List<Activity>> getUserActivities(Long userId) {
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<List<Activity>> activities = activityRepository.findALLByUserId(loginUser.getId());
        return new ResponseResult<>(HTTPResponse.SC_OK, "Fetched user activities successfully", activities.orElseGet(() -> new ArrayList<>()));
    }

    @Override
    public Activity createActivity(Activity activity) {
         activityRepository.save(activity);
         return activity;
    }

    @Override
    public int deleteVoteActivity(Long questionId, Long userId) {
        return activityRepository.deleteAllByQuestion_IdAndUserIdAndAction(questionId, userId, ActionType.VOTE);
    }
}
