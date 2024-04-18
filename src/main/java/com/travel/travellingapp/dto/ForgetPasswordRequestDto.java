package com.travel.travellingapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ForgetPasswordRequestDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Not a valid email")
    private String email;
}
