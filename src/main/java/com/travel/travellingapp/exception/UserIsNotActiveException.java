package com.travel.travellingapp.exception;

import org.springframework.http.HttpStatus;

public class UserIsNotActiveException extends RuntimeException {
    private final HttpStatus statusCode;
    public UserIsNotActiveException() {
        super("User is not active");
        this.statusCode = HttpStatus.BAD_REQUEST;
    }
    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
