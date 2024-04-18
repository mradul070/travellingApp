package com.travel.travellingapp.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyRegisterException extends RuntimeException {
    private final HttpStatus statusCode;
    public UserAlreadyRegisterException() {
        super("User is already registered");
        this.statusCode = HttpStatus.CONFLICT;
    }
    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
