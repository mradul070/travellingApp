package com.travel.travellingapp.dto;

import lombok.Data;

@Data
public class SignInResponseDto {
    private String accessToken;
    private String idToken;
    private String refreshToken;
}
