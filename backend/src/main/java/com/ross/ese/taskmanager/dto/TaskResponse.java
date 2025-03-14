package com.ross.ese.taskmanager.dto;

import com.ross.ese.taskmanager.model.TaskPriority;
import com.ross.ese.taskmanager.model.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private Long projectId;
    private String projectName;
}