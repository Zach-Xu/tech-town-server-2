package com.tech.service;

import com.tech.model.Activity;
import com.tech.vo.ResponseResult;

import java.util.List;

public interface ActivityService {
    ResponseResult<List<Activity>> getUserActivities(Long userId);

    Activity createActivity(Activity activity);

    int deleteVoteActivity(Long questionId, Long userId);
}
