package com.travel.travellingapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;

import lombok.Getter;

@Getter
@Configuration
public class CognitoConfig {
    @Value("${aws.accessToken}")
    private String accessToken;

    @Value("${aws.secretToken}")
    private String secretToken;

    @Value("${aws.sessionToken}")
    private String sessionToken;

    @Value("${aws.region}")
    private String region;
    
    @Value("${aws.clientId}")
    private String clientId;

    @Bean
    AWSCognitoIdentityProvider cognitoIdentityProvider() {
        BasicSessionCredentials awsCred = new BasicSessionCredentials(accessToken, secretToken, secretToken);
        AWSStaticCredentialsProvider awsStaticCredentialsProvider = new AWSStaticCredentialsProvider(awsCred);
       return AWSCognitoIdentityProviderClientBuilder.standard().withCredentials(awsStaticCredentialsProvider)
       .withRegion(region).build();
    }

}
