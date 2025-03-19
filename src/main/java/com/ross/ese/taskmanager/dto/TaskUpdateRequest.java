package com.ross.ese.taskmanager.dto;

import com.ross.ese.taskmanager.model.TaskPriority;
import com.ross.ese.taskmanager.model.TaskStatus;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskUpdateRequest {
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;
    
    private String description;
    
    private TaskStatus status;
    
    private TaskPriority priority;
    
    private LocalDateTime dueDate;
    
    private Long projectId;
}