package com.travel.travellingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.amazonaws.services.cognitoidp.model.SignUpResult;
import com.travel.travellingapp.dto.ConfirmForgetPasswordRequestDto;
import com.travel.travellingapp.dto.CreateUserRequestDto;
import com.travel.travellingapp.dto.SignInRequestDto;
import com.travel.travellingapp.dto.SignInResponseDto;
import com.travel.travellingapp.dto.VerifyOtpRequestDto;
import com.travel.travellingapp.exception.UserAlreadyRegisterException;
import com.travel.travellingapp.exception.UserIsNotActiveException;
import com.travel.travellingapp.exception.UserNotFoundException;
import com.travel.travellingapp.model.Users;
import com.travel.travellingapp.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AwsCognitoService awsCognitoService;
    public void registerUser(CreateUserRequestDto createUserDto) {
        try {
            Users findUserByEmail = userRepository.findByEmail(createUserDto.getEmail());
            log.info("");
            if (findUserByEmail != null && findUserByEmail.isActive()) {
                throw new UserAlreadyRegisterException();
            } else if (findUserByEmail != null && !findUserByEmail.isActive()) {
                awsCognitoService.sendVerificationCode(findUserByEmail.getEmail());
            } else {
                SignUpResult signUpResult = awsCognitoService.registerUser(createUserDto);
                Users user = new Users();
                user.setEmail(createUserDto.getEmail());
                user.setFirstName(createUserDto.getFirstName());
                user.setLastName(createUserDto.getLastName());
                user.setId(signUpResult.getUserSub());
                userRepository.save(user);
            }
        } catch (Exception e) {
            throw e;
        }
    }
    public void verifyOtp(VerifyOtpRequestDto verifyOtp) {
        try {
            Users findUserByEmail = userRepository.findByEmail(verifyOtp.getEmail());
            if (findUserByEmail == null) {
                throw new UserNotFoundException();
            }
            if (findUserByEmail != null && findUserByEmail.isActive()) {
                throw new UserAlreadyRegisterException();
            }
            awsCognitoService.ConfirmSignUpRequest(verifyOtp.getEmail(), verifyOtp.getOtp());
            findUserByEmail.setActive(true);
            userRepository.save(findUserByEmail);
        } catch (Exception e) {
            throw e;
        }
    }
    public SignInResponseDto login(@Valid @RequestBody SignInRequestDto signInRequestDto) {
        try {
            Users findUserByEmail = userRepository.findByEmail(signInRequestDto.getEmail());
            if (findUserByEmail == null) {
                throw new UserNotFoundException();
            }
            if (findUserByEmail != null && !findUserByEmail.isActive()) {
                throw new UserIsNotActiveException();
            }
            SignInResponseDto signInResponseDto = awsCognitoService.signIn(signInRequestDto);
            return signInResponseDto;
        } catch (Exception e) {
            throw e;
        }
    }
    public void forgetPassword(String email) {
        try {
            Users findUserByEmail = userRepository.findByEmail(email);
            if (findUserByEmail == null) {
                throw new UserNotFoundException();
            }
            if (findUserByEmail != null && !findUserByEmail.isActive()) {
                throw new UserIsNotActiveException();
            }
            awsCognitoService.ForgetPassword(email);
        } catch (Exception e) {
            throw e;
        }
    }
    public void confirmForgetPassword(ConfirmForgetPasswordRequestDto confirmForgetPasswordRequestDto) {
        Users findUserByEmail = userRepository.findByEmail(confirmForgetPasswordRequestDto.getEmail());
        if (findUserByEmail == null) {
            throw new UserNotFoundException();
        }
        if (findUserByEmail != null && !findUserByEmail.isActive()) {
            throw new UserIsNotActiveException();
        }
        awsCognitoService.confirmForgetPassword(confirmForgetPasswordRequestDto.getEmail(), confirmForgetPasswordRequestDto.getOtp(), confirmForgetPasswordRequestDto.getPassword());
    }

    public void changePassword() {

    }
}
