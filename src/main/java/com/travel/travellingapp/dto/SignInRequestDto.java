
package com.travel.travellingapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern.Flag;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignInRequestDto {

  @NotBlank(message = "Email address is required.")
  @Email(message = "The email address is invalid.", flags = { Flag.CASE_INSENSITIVE })
  private String email;
  
  @NotBlank(message = "Password is required.")
  @Size(min = 8, max = 16, message = "Password must be at least 8 character long and maximum 16 character long.")
  private String password;
}