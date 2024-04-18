package com.travel.travellingapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VerifyOtpRequestDto {
    @NotBlank(message = "Email is required to validate user")
    @Email(message = "Invalid Email Address")
    private String email;

    @NotBlank(message = "Otp is required")
    @Size(min = 6, max = 6 ,message = "Invalid OTP, must be exactly 6 characters")
    private String otp;
}
