package com.ross.ese.taskmanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

// Data Transfer Object for user registration requests.
@Data
public class SignupRequest {
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank(message = "Email cannot be empty")
    @Size(max = 50, message = "Email cannot exceed 50 characters")
    @Email(message = "Email must be valid")
    private String email;
    
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
    private String password;
}
