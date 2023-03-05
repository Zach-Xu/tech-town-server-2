package com.tech.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.dto.ResponseResult;
import com.tech.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseResult result = new ResponseResult(HttpStatus.FORBIDDEN.value(),"No permission to perform such action");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(result);
        WebUtils.renderString(response,json);
    }
}
