package com.ross.ese.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Data Transfer Object for simple message responses.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    private String message;
}