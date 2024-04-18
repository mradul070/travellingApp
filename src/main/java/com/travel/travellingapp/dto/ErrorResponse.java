package com.travel.travellingapp.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter

public class ErrorResponse {
    private int status;
    private boolean success;
    private Object error;

    public ErrorResponse(int status, Object error) {
        this.error = error;
        this.success = false;
        this.status = status;
    }
}
