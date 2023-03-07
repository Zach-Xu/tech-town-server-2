package com.tech.controller;



import com.tech.model.User;
import com.tech.service.AuthService;
import com.tech.utils.WebUtils;
import com.tech.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user) {
        return authService.login(user);
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody @Valid User user, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            String messages = WebUtils.getErrorMessages(bindingResult);
            throw new IllegalArgumentException(messages);
        }
        return  authService.register(user);
    }

    @GetMapping("/refresh")
    public ResponseResult refresh(){
        return authService.getTokenUser();
    }

}
