package com.ross.ese.taskmanager.controller;

import com.ross.ese.taskmanager.dto.JwtResponse;
import com.ross.ese.taskmanager.dto.LoginRequest;
import com.ross.ese.taskmanager.dto.MessageResponse;
import com.ross.ese.taskmanager.dto.SignupRequest;
import com.ross.ese.taskmanager.service.SupabaseAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

/**
 * REST controller for authentication operations such as signup and signin.
 * This controller integrates with Supabase authentication through the service
 * layer.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final SupabaseAuthService authService;
    
    @Value("${app.auth.dev-mode:false}")
    private boolean devMode;

    public AuthController(SupabaseAuthService authService) {
        this.authService = authService;
    }

    /**
     * Register a new user
     * 
     * @param signupRequest Contains username, email, and password
     * @return JWT token if successful, error message if failed
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        try {
            String token = authService.signUp(
                    signupRequest.getEmail(),
                    signupRequest.getPassword(),
                    signupRequest.getUsername());
    
            if (token == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new MessageResponse("Error: Registration failed"));
            }
    
            return ResponseEntity.ok(new JwtResponse(
                    token,
                    signupRequest.getUsername(),
                    signupRequest.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    /**
     * Authenticate an existing user
     * 
     * @param loginRequest Contains email and password
     * @return JWT token if successful, error message if failed
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            String token = authService.signIn(loginRequest.getEmail(), loginRequest.getPassword());

            if (token != null) {
                return ResponseEntity.ok(new JwtResponse(token, null, loginRequest.getEmail()));
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Error: Invalid credentials"));

        } catch (RuntimeException e) {
            // Check for email confirmation error
            if (e.getMessage() != null && e.getMessage().contains("email_not_confirmed")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new MessageResponse(
                                "Please check your email and confirm your account before logging in"));
            }

            // Use configuration-based development bypass
            if (devMode) {
                return ResponseEntity.ok(new JwtResponse(
                        "dev-login-" + UUID.randomUUID().toString(),
                        null,
                        loginRequest.getEmail()));
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    /**
     * Verify if a token is valid
     * 
     * @param token The JWT token to verify
     * @return Status indicating if the token is valid
     */
    @GetMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Invalid token format"));
        }

        String tokenValue = token.substring(7);
        boolean isValid = authService.verifyToken(tokenValue);

        return ResponseEntity.ok(new MessageResponse(isValid ? "Token is valid" : "Token is invalid"));
    }
}