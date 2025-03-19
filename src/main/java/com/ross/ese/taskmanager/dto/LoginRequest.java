package com.ross.ese.taskmanager.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

//Data Transfer Object for user login requests.
@Data
public class LoginRequest {
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email must be valid")
    private String email;
    
    @NotBlank(message = "Password cannot be empty")
    private String password;
}