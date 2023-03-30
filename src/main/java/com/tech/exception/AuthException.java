package com.tech.exception;

import com.nimbusds.oauth2.sdk.http.HTTPResponse;

public class AuthException extends RuntimeException{

    public static final int CODE = HTTPResponse.SC_FORBIDDEN;

    public AuthException(String errorMsg) {
        super(errorMsg);
    }
}
