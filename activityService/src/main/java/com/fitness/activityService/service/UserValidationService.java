package com.fitness.activityService.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserValidationService {

    private final WebClient userServiceWebClient;

    public boolean validateUser(String userId){
        log.info("Calling user validation API for userID : {}", userId);
        try{

            return  userServiceWebClient.get()
                    .uri("api/users/{userId}/validate", userId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                // User not found → treat as invalid
                throw new RuntimeException("User Not Found: " +  userId);
            }else if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                // User not found → treat as invalid
                throw new RuntimeException("Invalid Request: " +  userId);
            }else {
                throw new RuntimeException("Exception Request: " +  e.getMessage(),e);
            }

        }
    }
}
