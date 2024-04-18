package com.travel.travellingapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern.Flag;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class ConfirmForgetPasswordRequestDto {
    @NotBlank(message = "Email address is required.")
    @Email(message = "The email address is invalid.", flags = { Flag.CASE_INSENSITIVE })
    private String email;
    
    @NotBlank(message = "Otp is required")
    @Size(min = 6 , max = 6, message = "Otp must be excatly 6 character")
    private String otp;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 16, message = "Password must be at least 8 character long and maximum 16 character long.")
    private String password;
}
