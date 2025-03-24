package com.ross.ese.taskmanager.service.impl;

import com.ross.ese.taskmanager.service.SupabaseAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the SupabaseAuthService interface using direct REST API
 * calls to Supabase Auth.
 * This provides an enterprise-grade integration with Supabase Auth.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SupabaseAuthServiceImpl implements SupabaseAuthService {

    private final WebClient supabaseAuthClient;

    @Override
    public String signUp(String email, String password, String username) {
        try {
            log.debug("Attempting to sign up user with email: {}", email);
            
            // Create request payload
            Map<String, Object> payload = new HashMap<>();
            payload.put("email", email);
            payload.put("password", password);
        
            Map<String, Object> userMetadata = new HashMap<>();
            userMetadata.put("full_name", username);
            userMetadata.put("username", username);
            payload.put("data", userMetadata);
            
            log.debug("Registration payload (excluding sensitive data): {email: {}, data: {}}", 
                      email, userMetadata);
            
            // Make the API call
            return supabaseAuthClient.post()
                    .uri("/auth/v1/signup")
                    .body(BodyInserters.fromValue(payload))
                    .retrieve()
                    .onStatus(
                        HttpStatusCode::isError,
                        (ClientResponse response) -> response.bodyToMono(String.class)
                            .flatMap(body -> {
                                log.error("Supabase signup error: {} - {}", 
                                          response.statusCode(), body);
                                return Mono.error(new RuntimeException(
                                    "Supabase signup failed: " + response.statusCode() + " - " + body));
                            })
                    )
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .map(response -> {
                        log.debug("Supabase signup response received: {}", 
                                 response.keySet().toString());
                        if (response.containsKey("access_token")) {
                            return (String) response.get("access_token");
                        } else if (response.containsKey("id")) {
                            log.info("User created but no access token returned");
                            return "registered"; // Return a placeholder when user is created but no token
                        } else {
                            log.warn("Signup response did not contain access_token or id: {}", response);
                            return null;
                        }
                    })
                    .block();
                
        } catch (Exception e) {
            log.error("Error signing up user: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public String signIn(String email, String password) {
        try {
            log.debug("Attempting to sign in user with email: {}", email);
            
            // Create request payload
            Map<String, Object> payload = new HashMap<>();
            payload.put("email", email);
            payload.put("password", password);
            
            // Make the API call
            return supabaseAuthClient.post()
                    .uri("/auth/v1/token?grant_type=password")
                    .body(BodyInserters.fromValue(payload))
                    .retrieve()
                    .onStatus(
                        HttpStatusCode::isError,
                        (ClientResponse response) -> response.bodyToMono(String.class)
                            .flatMap(body -> {
                                log.error("Supabase signin error: {} - {}", 
                                          response.statusCode(), body);
                                return Mono.error(new RuntimeException(
                                    "Supabase signin failed: " + response.statusCode() + " - " + body));
                            })
                    )
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .map(response -> {
                        if (response.containsKey("access_token")) {
                            return (String) response.get("access_token");
                        } else {
                            log.warn("Signin response did not contain access_token: {}", 
                                     response.keySet().toString());
                            return null;
                        }
                    })
                    .block();

        } catch (Exception e) {
            log.error("Error signing in user: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean verifyToken(String token) {
        try {
            log.debug("Verifying token validity");
            
            // Make the API call to verify the token
            return supabaseAuthClient.get()
                    .uri("/auth/v1/user")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .onStatus(
                        HttpStatusCode::isError,
                        (ClientResponse response) -> {
                            log.debug("Token invalid: {}", response.statusCode());
                            return Mono.empty();
                        }
                    )
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .map(response -> {
                        boolean isValid = response != null && response.containsKey("id");
                        log.debug("Token validity check result: {}", isValid);
                        return isValid;
                    })
                    .onErrorReturn(false)
                    .block();
                    
        } catch (Exception e) {
            log.debug("Token verification failed with exception: {}", e.getMessage());
            return false;
        }
    }
}
