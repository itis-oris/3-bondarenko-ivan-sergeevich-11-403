package com.itis.cryptotracker.exception;

import org.springframework.http.HttpStatus;

public class ExternalServiceException extends ServiceException {

    public ExternalServiceException(String message) {
        super(HttpStatus.BAD_GATEWAY, message);
    }

    public ExternalServiceException(String message, Throwable cause) {
        super(HttpStatus.BAD_GATEWAY, message, cause);
    }
}
