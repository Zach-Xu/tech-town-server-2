package com.tech.handler;

import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.tech.exception.NotFoundException;
import com.tech.vo.ResponseResult;
import com.tech.exception.AuthException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {


    @ExceptionHandler({AuthException.class, UsernameNotFoundException.class})
    public ResponseResult authExceptionHandler(Exception e) {
        return new ResponseResult(AuthException.CODE, e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseResult notFoundExceptionHandler(NotFoundException e) {
        return new ResponseResult(NotFoundException.CODE, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseResult illegalArgumentExceptionHandler(IllegalArgumentException e) {
        return new ResponseResult(HTTPResponse.SC_BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e) {
        return new ResponseResult(HTTPResponse.SC_BAD_REQUEST, e.getMessage());
    }

}
