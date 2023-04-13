package com.tech.controller;

import com.tech.service.UserService;
import com.tech.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public ResponseResult getUsersByKeyword(@RequestParam("keyword") String keyword){
        ResponseResult usersByKeyword = userService.getUsersByKeyword(keyword);
        return usersByKeyword;
    }
}
