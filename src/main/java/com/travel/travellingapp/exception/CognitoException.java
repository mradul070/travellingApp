package com.travel.travellingapp.exception;

public class CognitoException extends RuntimeException {
    private final int statusCode;
    public CognitoException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    public int getStatusCode() {
        return statusCode;
    }
}
