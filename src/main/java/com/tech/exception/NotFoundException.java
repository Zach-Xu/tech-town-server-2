package com.tech.exception;

import com.nimbusds.oauth2.sdk.http.HTTPResponse;

public class NotFoundException extends  RuntimeException{

    public static final int CODE = HTTPResponse.SC_NOT_FOUND;

    public NotFoundException(String errorMsg) {
        super(errorMsg);
    }
}
