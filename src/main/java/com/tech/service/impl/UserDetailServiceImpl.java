package com.tech.service.impl;

import com.tech.model.LoginUser;
import com.tech.model.User;
import com.tech.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        LoginUser loginUser = user.map(LoginUser::new).orElseThrow(()-> new RuntimeException("Incorrect email or password"));
        return loginUser;
    }
}