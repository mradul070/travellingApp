package com.travel.travellingapp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travel.travellingapp.dto.ApiResponse;
import com.travel.travellingapp.dto.ConfirmForgetPasswordRequestDto;
import com.travel.travellingapp.dto.CreateUserRequestDto;
import com.travel.travellingapp.dto.ForgetPasswordRequestDto;
import com.travel.travellingapp.dto.SignInRequestDto;
import com.travel.travellingapp.dto.SignInResponseDto;
import com.travel.travellingapp.dto.VerifyOtpRequestDto;
import com.travel.travellingapp.service.UserService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody CreateUserRequestDto createUserBody) {
        userService.registerUser(createUserBody);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.CREATED.value(), null, "User Created successfully");
        log.info("User created successfully");
        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.CREATED);

    }

    @PostMapping("verify-otp")
    public ResponseEntity<ApiResponse> verifyUserOtp(@Valid @RequestBody VerifyOtpRequestDto verifyOtpDto) {
        userService.verifyOtp(verifyOtpDto);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), null, "Verification Successfull");
        log.info("Verification otp send successfully");
        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);

    }

    @PostMapping("login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody SignInRequestDto signInRequestDto) {
        SignInResponseDto signInResponseDto = userService.login(signInRequestDto);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), signInResponseDto, "Login Successfull");
        log.info("User login successfully");
        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);

    }

    @PostMapping("forget-password")
    public ResponseEntity<ApiResponse> forgetPassword(
            @Valid @RequestBody ForgetPasswordRequestDto forgetPasswordRequestDto) {
        userService.forgetPassword(forgetPasswordRequestDto.getEmail());
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), null,
                "Otp send successfully to" + forgetPasswordRequestDto.getEmail());
        log.info("Reset password otp send successfully");
        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("confirm-forget-password")
    public ResponseEntity<ApiResponse> confirmForgetPassword(
            @Valid @RequestBody ConfirmForgetPasswordRequestDto confirmForgetPasswordRequestDto) {
        userService.confirmForgetPassword(confirmForgetPasswordRequestDto);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), null, "Password updated succcessfully");
        log.info("Confirm password completed");
        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
    }
}
