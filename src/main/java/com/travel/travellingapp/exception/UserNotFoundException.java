package com.travel.travellingapp.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends RuntimeException {
    private final HttpStatus statusCode;
    public UserNotFoundException() {
        super("User not found please register");
        this.statusCode = HttpStatus.NOT_FOUND;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
