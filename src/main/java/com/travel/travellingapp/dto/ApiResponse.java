package com.travel.travellingapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse {
    private int status;
    private boolean success;
    private Object data;
    private String message;

    public ApiResponse(int status, Object data, String message) {
        this.status = status;
        this.success = true;
        this.data = data;
        this.message = message;
    }
}
