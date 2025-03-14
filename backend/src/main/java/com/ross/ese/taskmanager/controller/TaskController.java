package com.ross.ese.taskmanager.controller;

import com.ross.ese.taskmanager.dto.TaskCreateRequest;
import com.ross.ese.taskmanager.dto.TaskResponse;
import com.ross.ese.taskmanager.dto.TaskUpdateRequest;
import com.ross.ese.taskmanager.model.Task;
import com.ross.ese.taskmanager.model.TaskStatus;
import com.ross.ese.taskmanager.repository.ProjectRepository;
import com.ross.ese.taskmanager.repository.TaskRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {
    
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    
    public TaskController(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }
    
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        List<TaskResponse> responses = tasks.stream()
            .map(this::convertToTaskResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        return taskRepository.findById(id)
                .map(this::convertToTaskResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskCreateRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        task.setCreatedAt(LocalDateTime.now());
        task.setStatus(TaskStatus.TODO);  // Default status for new tasks
        
        // Set project if provided
        if (request.getProjectId() != null) {
            projectRepository.findById(request.getProjectId())
                    .ifPresent(task::setProject);
        }
        
        Task savedTask = taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertToTaskResponse(savedTask));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskUpdateRequest request) {
        
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        
        // Update fields if provided
        if (request.getTitle() != null) existingTask.setTitle(request.getTitle());
        if (request.getDescription() != null) existingTask.setDescription(request.getDescription());
        if (request.getStatus() != null) existingTask.setStatus(request.getStatus());
        if (request.getPriority() != null) existingTask.setPriority(request.getPriority());
        if (request.getDueDate() != null) existingTask.setDueDate(request.getDueDate());
        
        // Update project if provided
        if (request.getProjectId() != null) {
            projectRepository.findById(request.getProjectId())
                    .ifPresent(existingTask::setProject);
        }
        
        Task updatedTask = taskRepository.save(existingTask);
        return ResponseEntity.ok(convertToTaskResponse(updatedTask));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        taskRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskResponse>> getTasksByProject(@PathVariable Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            return ResponseEntity.notFound().build();
        }
        
        List<Task> tasks = taskRepository.findByProjectId(projectId);
        List<TaskResponse> taskResponses = tasks.stream()
                .map(this::convertToTaskResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(taskResponses);
    }
    
    // Helper method to convert Task to TaskResponse
    private TaskResponse convertToTaskResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getCreatedAt(),
                Optional.ofNullable(task.getProject()).map(project -> project.getId()).orElse(null),
                Optional.ofNullable(task.getProject()).map(project -> project.getName()).orElse(null)
        );
    }
}