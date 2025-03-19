package com.ross.ese.taskmanager.service.impl;

import com.ross.ese.taskmanager.service.SupabaseAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the SupabaseAuthService interface using direct REST API calls.
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
            // Create request payload
            Map<String, Object> userMetadata = new HashMap<>();
            userMetadata.put("username", username);
            
            Map<String, Object> payload = new HashMap<>();
            payload.put("email", email);
            payload.put("password", password);
            payload.put("data", userMetadata);
            // Make the API call
            Map<String, Object> response = supabaseAuthClient.post()
                .uri("/signup")
                .body(BodyInserters.fromValue(payload))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
            
            if (response != null && response.containsKey("access_token")) {
                return (String) response.get("access_token");
            }
            
            log.warn("Signup response did not contain access token");
            return null;
        } catch (Exception e) {
            log.error("Error signing up user: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public String signIn(String email, String password) {
        try {
            // Create request payload
            Map<String, Object> payload = new HashMap<>();
            payload.put("email", email);
            payload.put("password", password);
            // Make the API call
            Map<String, Object> response = supabaseAuthClient.post()
                .uri("/token?grant_type=password")
                .body(BodyInserters.fromValue(payload))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
            
            if (response != null && response.containsKey("access_token")) {
                return (String) response.get("access_token");
            }
            
            log.warn("Signin response did not contain access token");
            return null;
        } catch (Exception e) {
            log.error("Error signing in user: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public boolean verifyToken(String token) {
        try {
            // Make the API call to verify the token
            Map<String, Object> response = supabaseAuthClient.get()
                .uri("/user")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
            
            return response != null && response.containsKey("id");
        } catch (Exception e) {
            log.debug("Token verification failed: {}", e.getMessage());
            return false;
        }
    }
}