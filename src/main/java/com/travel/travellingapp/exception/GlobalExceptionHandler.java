package com.travel.travellingapp.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

import com.travel.travellingapp.dto.ErrorResponse;
import com.travel.travellingapp.helper.HttpStatusConverter;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        ArrayList<HashMap<String, String>> errorMessages = new ArrayList<>();
        e.getBindingResult().getFieldErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();
            HashMap<String, String> errors = new HashMap<>();
            if(errorMessage != null && !errorMessage.isEmpty()) {
                errors.put("error", errorMessage);
                errors.put("field", error.getField());
                errorMessages.add(errors);
            }
        });
        ErrorResponse response = new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), errorMessages);
        return new ResponseEntity<ErrorResponse>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    
    @ExceptionHandler(CognitoException.class)
    public ResponseEntity<ErrorResponse> handleCognitoException(CognitoException e) {
        ErrorResponse response = new ErrorResponse(e.getStatusCode(), e.getMessage());
        return new ResponseEntity<ErrorResponse>(response, HttpStatusConverter.fromStatusCode(e.getStatusCode()));
    }

    @ExceptionHandler(UserAlreadyRegisterException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyRegistered(UserAlreadyRegisterException e) {
        ErrorResponse response = new ErrorResponse(e.getStatusCode().value(), e.getMessage());
        return new ResponseEntity<ErrorResponse>(response, e.getStatusCode());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException e) {
        ErrorResponse response = new ErrorResponse(e.getStatusCode().value(), e.getMessage());
        return new ResponseEntity<ErrorResponse>(response, e.getStatusCode());
    }

    @ExceptionHandler(UserIsNotActiveException.class)
    public ResponseEntity<ErrorResponse> handleNotActiceException(UserIsNotActiveException e) {
        ErrorResponse response = new ErrorResponse(e.getStatusCode().value(), e.getMessage());
        return new ResponseEntity<ErrorResponse>(response, e.getStatusCode());
    }

}

