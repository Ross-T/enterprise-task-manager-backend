package com.ross.ese.taskmanager.service;

import com.ross.ese.taskmanager.dto.TaskCreateRequest;
import com.ross.ese.taskmanager.dto.TaskResponse;
import com.ross.ese.taskmanager.dto.TaskUpdateRequest;
import com.ross.ese.taskmanager.model.TaskStatus;

import java.util.List;

public interface TaskService {
    List<TaskResponse> getAllTasks();
    TaskResponse getTaskById(Long id);
    TaskResponse createTask(TaskCreateRequest request);
    TaskResponse updateTask(Long id, TaskUpdateRequest request);
    void deleteTask(Long id);
    List<TaskResponse> getTasksByProject(Long projectId);
    List<TaskResponse> getTasksByStatus(TaskStatus status);
}