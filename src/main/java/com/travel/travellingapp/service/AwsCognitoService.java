package com.travel.travellingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.services.cognitoidentity.model.AmazonCognitoIdentityException;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.CodeDeliveryFailureException;
import com.amazonaws.services.cognitoidp.model.CodeMismatchException;
import com.amazonaws.services.cognitoidp.model.ConfirmForgotPasswordRequest;
import com.amazonaws.services.cognitoidp.model.ConfirmSignUpRequest;
import com.amazonaws.services.cognitoidp.model.ConfirmSignUpResult;
import com.amazonaws.services.cognitoidp.model.ExpiredCodeException;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordRequest;
import com.amazonaws.services.cognitoidp.model.InitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.InvalidPasswordException;
import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.amazonaws.services.cognitoidp.model.ResendConfirmationCodeRequest;
import com.amazonaws.services.cognitoidp.model.ResendConfirmationCodeResult;
import com.amazonaws.services.cognitoidp.model.SignUpRequest;
import com.amazonaws.services.cognitoidp.model.SignUpResult;
import com.amazonaws.services.cognitoidp.model.UserNotConfirmedException;
import com.amazonaws.services.cognitoidp.model.UserNotFoundException;
import com.amazonaws.services.cognitoidp.model.UsernameExistsException;
import com.travel.travellingapp.config.CognitoConfig;
import com.travel.travellingapp.dto.CreateUserRequestDto;
import com.travel.travellingapp.dto.SignInRequestDto;
import com.travel.travellingapp.dto.SignInResponseDto;
import com.travel.travellingapp.exception.CognitoException;

import java.util.Map;
import java.util.HashMap;

@Service
public class AwsCognitoService {
    @Autowired
    private AWSCognitoIdentityProvider cognitoIdentityProvider;
    @Autowired
    private CognitoConfig cognitoConfig;
    public SignUpResult registerUser(CreateUserRequestDto createUserBody) {
        try {
            SignUpRequest signUpRequest = new SignUpRequest()
            .withClientId(cognitoConfig.getClientId()).
            withUsername(createUserBody.getEmail())
            .withPassword(createUserBody.getPassword())
            .withUserAttributes(
                new AttributeType().withName("given_name").withValue(createUserBody.getFirstName()),
                new AttributeType().withName("family_name").withValue(createUserBody.getLastName())
            );
            SignUpResult signUpResult = cognitoIdentityProvider.signUp(signUpRequest);
            return signUpResult;
        } catch (UsernameExistsException e) {
            throw new CognitoException("An account with the given email already exists.", e.getStatusCode());
        } catch (InvalidPasswordException e) {
            throw new CognitoException("Invalid Password format.", e.getStatusCode());
        } catch (AmazonCognitoIdentityException e) {
            throw new CognitoException(e.getMessage(), e.getStatusCode());
        } catch (CodeDeliveryFailureException e) {
            throw new CognitoException("Failed to send configuration code try again ", e.getStatusCode());
        } catch(Exception e) {
            throw e;
        }
    }
    public void sendVerificationCode(String email) {
        try {
            ResendConfirmationCodeRequest resendConfirmationCodeRequest =  new ResendConfirmationCodeRequest().withClientId(cognitoConfig.getClientId()).withUsername(email);
            ResendConfirmationCodeResult resendConfirmationCodeResult = cognitoIdentityProvider.resendConfirmationCode(resendConfirmationCodeRequest);
            System.out.println(resendConfirmationCodeResult);
        } catch (UserNotFoundException e) {
            throw new CognitoException("An account with the given email already exists.", e.getStatusCode());
        } catch (CodeDeliveryFailureException e) {
            throw new CognitoException("Failed to send configuration code try again.", e.getStatusCode());
        } catch(AmazonCognitoIdentityException e) {
            throw new CognitoException("Verification code Expired.", e.getStatusCode());
        } catch(Exception e) {
            throw e;
        }

    }

    public void ConfirmSignUpRequest(String email, String confirmationCode) {
        try {
            ConfirmSignUpRequest confirmSignUpRequest = new ConfirmSignUpRequest().withClientId(cognitoConfig.getClientId()).withUsername(email).withConfirmationCode(confirmationCode);
            ConfirmSignUpResult confirmationSignzupResult =  cognitoIdentityProvider.confirmSignUp(confirmSignUpRequest);
            System.out.println(confirmationSignzupResult);
        } catch (UserNotFoundException e) {
            throw new CognitoException("An account with the given email already exists.", e.getStatusCode());
        } catch(CodeMismatchException e) {
            throw new CognitoException("Verification code validation fail.", e.getStatusCode());
        } catch(ExpiredCodeException e) {
            throw new CognitoException("Verification code Expired try again.", e.getStatusCode());
        } catch(AmazonCognitoIdentityException e) {
            throw new CognitoException("Verification code Expired.", e.getStatusCode());
        } catch(Exception e) {
            throw e;
        }
    }
    public SignInResponseDto signIn(SignInRequestDto signInRequestDto) {
        try {
            Map<String, String> authParameters = new HashMap<>();
            authParameters.put("USERNAME", signInRequestDto.getEmail());
            authParameters.put("PASSWORD", signInRequestDto.getPassword());
                        InitiateAuthRequest initiateAuthRequest = new InitiateAuthRequest().withAuthFlow(AuthFlowType.USER_PASSWORD_AUTH).
            withClientId(cognitoConfig.getClientId())
            .withAuthParameters(authParameters);
            InitiateAuthResult authResult = cognitoIdentityProvider.initiateAuth(initiateAuthRequest);
            AuthenticationResultType authenticationResult = authResult.getAuthenticationResult();
            System.out.println(authenticationResult);
            SignInResponseDto signInResponseDto = new SignInResponseDto();
            signInResponseDto.setAccessToken(authenticationResult.getAccessToken());
            signInResponseDto.setIdToken(authenticationResult.getIdToken());
            signInResponseDto.setRefreshToken(authenticationResult.getRefreshToken());
            return signInResponseDto;
        } catch (UserNotConfirmedException e) {
            throw new CognitoException("User is not registered", e.getStatusCode());
        } catch(UserNotFoundException e) {
            throw new CognitoException("User is not found", e.getStatusCode());
        } catch(NotAuthorizedException e) {
            throw new CognitoException("Invalid User name and password", e.getStatusCode());
        } catch(Exception e) {
            throw e;
        }
    }

    public void ForgetPassword(String email) {
        try {
            ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest()
            .withClientId(cognitoConfig.getClientId())
            .withUsername(email);
        cognitoIdentityProvider.forgotPassword(forgotPasswordRequest);
        } catch (UserNotFoundException e) {
            throw new CognitoException("User not found exception", e.getStatusCode());
        } catch (CodeDeliveryFailureException e) {
            throw new CognitoException("Failed to send configuration code try again ", e.getStatusCode());
        } catch(Exception e) {
            throw e;
        }
    }
    public void confirmForgetPassword(String email, String code, String password) {
        try {
            ConfirmForgotPasswordRequest confirmForgotPasswordRequest = new ConfirmForgotPasswordRequest()
            .withClientId(cognitoConfig.getClientId())
            .withUsername(email)
            .withPassword(password)
            .withConfirmationCode(code);
            cognitoIdentityProvider.confirmForgotPassword(confirmForgotPasswordRequest);    
        } catch (CodeMismatchException e) {
            throw new CognitoException("Verification code validation fail.", e.getStatusCode());
        } catch (ExpiredCodeException e) {
            throw new CognitoException("Verification code Expired try again.", e.getStatusCode());
        } catch(InvalidPasswordException e) {
            throw new CognitoException("Invalid Password format.", e.getStatusCode());
        } catch(UserNotFoundException e) {
            throw new CognitoException("User not found", e.getStatusCode());
        } catch(Exception e) {
            throw e;
        }
    }
}
