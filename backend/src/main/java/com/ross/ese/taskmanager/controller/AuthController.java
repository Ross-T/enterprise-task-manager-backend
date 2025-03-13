package com.ross.ese.taskmanager.controller;

import com.ross.ese.taskmanager.dto.JwtResponse;
import com.ross.ese.taskmanager.dto.LoginRequest;
import com.ross.ese.taskmanager.dto.MessageResponse;
import com.ross.ese.taskmanager.dto.SignupRequest;
import com.ross.ese.taskmanager.service.SupabaseAuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication operations such as signup and signin.
 * This controller integrates with Supabase authentication through the service layer.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final SupabaseAuthService authService;

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
        String token = authService.signUp(
            signupRequest.getEmail(), 
            signupRequest.getPassword(), 
            signupRequest.getUsername()
        );
        
        if (token == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Error: Registration failed"));
        }
        
        return ResponseEntity.ok(new JwtResponse(
            token, 
            signupRequest.getUsername(), 
            signupRequest.getEmail()
        ));
    }
    
    /**
     * Authenticate an existing user
     * 
     * @param loginRequest Contains email and password
     * @return JWT token if successful, error message if failed
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        String token = authService.signIn(loginRequest.getEmail(), loginRequest.getPassword());
        
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Error: Invalid credentials"));
        }
        
        return ResponseEntity.ok(new JwtResponse(token, null, loginRequest.getEmail()));
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