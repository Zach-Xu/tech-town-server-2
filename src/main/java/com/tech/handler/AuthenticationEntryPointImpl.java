package com.tech.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.utils.WebUtils;
import com.tech.dto.ResponseResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseResult result = new ResponseResult(HttpStatus.UNAUTHORIZED.value(), "Authentication fail");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(result);
        WebUtils.renderString(response, json);
    }
}
