package com.tech.controller;

import com.tech.dto.ResponseResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("hello")
    public ResponseResult hello (){
        return new ResponseResult<>(200,"Hello");
    }
}
