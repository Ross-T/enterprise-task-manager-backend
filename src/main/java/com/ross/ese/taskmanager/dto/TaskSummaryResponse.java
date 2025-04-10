package com.ross.ese.taskmanager.dto;

import com.ross.ese.taskmanager.model.TaskPriority;
import com.ross.ese.taskmanager.model.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO for task lists
 * to reduce payload size when returning multiple tasks.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskSummaryResponse {
    private Long id;
    private String title;
    private TaskStatus status;
    private TaskPriority priority;
    private Long projectId;
    private String projectName;
    
}
