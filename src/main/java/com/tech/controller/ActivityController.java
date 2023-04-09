package com.tech.controller;

import com.tech.model.Activity;
import com.tech.service.ActivityService;
import com.tech.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping("/user/{userId}")
    ResponseResult<List<Activity>>  getUserActivities(@PathVariable("userId") Long userId){
        return activityService.getUserActivities(userId);
    }


}
