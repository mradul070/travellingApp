package com.travel.travellingapp.helper;

import org.springframework.http.HttpStatus;

public class HttpStatusConverter {

    public static HttpStatus fromStatusCode(int statusCode) {
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (IllegalArgumentException e) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
