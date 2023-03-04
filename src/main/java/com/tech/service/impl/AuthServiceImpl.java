package com.tech.service.impl;

import com.tech.dto.UserResponse;
import com.tech.model.LoginUser;
import com.tech.model.User;
import com.tech.repo.UserRepository;
import com.tech.service.AuthService;
import com.tech.utils.JwtUtils;
import com.tech.dto.ResponseResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public ResponseResult login(User user) {
        // Authenticate user
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authToken);

        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("Login fail");
        }

        // Authenticate successful
        LoginUser loginUser = (LoginUser)authenticate.getPrincipal();

        // Generate Jwt
        User userDB =loginUser.getUser();
        String token = JwtUtils.createJWT(userDB);

        // Prepare DTO for response
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(userDB, userResponse);

        Map<String, Object> loginResponseData = new HashMap<>();
        loginResponseData.put("user", userResponse);
        loginResponseData.put("token", token);

        // return token along with user info
        return new ResponseResult(200, "Login Successful", loginResponseData);

    }

    @Override
    public ResponseResult register(User user) {
        Optional<User> userDB = userRepository.findByEmail(user.getEmail());
        userDB.ifPresent( u -> {
            throw  new RuntimeException("email: " + u.getEmail() + " already exists");
        });

        // encode password
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // store user in database and generate Jwt
        User newUser = userRepository.save(user);
        String token = JwtUtils.createJWT(user);

        // Prepare DTO for response
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(userDB, userResponse);

        Map<String, Object> RegisterResponseData = new HashMap<>();
        RegisterResponseData.put("user", userResponse);
        RegisterResponseData.put("token", token);

        // return token along with user info
        return new ResponseResult(201, "Register successful", RegisterResponseData);
    }

    @Override
    public ResponseResult logout() {
        return null;
    }
}
