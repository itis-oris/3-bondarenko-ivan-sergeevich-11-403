package com.itis.cryptotracker.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ServiceException {

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
