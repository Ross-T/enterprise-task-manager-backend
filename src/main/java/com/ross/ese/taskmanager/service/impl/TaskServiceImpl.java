package com.ross.ese.taskmanager.service.impl;

import com.ross.ese.taskmanager.dto.TaskCreateRequest;
import com.ross.ese.taskmanager.dto.TaskResponse;
import com.ross.ese.taskmanager.dto.TaskUpdateRequest;
import com.ross.ese.taskmanager.model.Project;
import com.ross.ese.taskmanager.model.Task;
import com.ross.ese.taskmanager.model.TaskStatus;
import com.ross.ese.taskmanager.repository.ProjectRepository;
import com.ross.ese.taskmanager.repository.TaskRepository;
import com.ross.ese.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final CacheManager cacheManager;

    @Override
    @Cacheable(value = "tasks", key = "'all'")
    public List<TaskResponse> getAllTasks() {
        log.debug("Fetching all tasks");
        return taskRepository.findAll().stream()
                .map(this::convertToTaskResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "tasks", key = "#id")
    public TaskResponse getTaskById(Long id) {
        log.debug("Fetching task with id: {}", id);
        return taskRepository.findById(id)
                .map(this::convertToTaskResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Task not found with id: " + id));
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "tasks", key = "'all'"),
            @CacheEvict(value = "tasks", key = "'project-' + #result.projectId", condition = "#result.projectId != null"),
            @CacheEvict(value = "projects", key = "#result.projectId", condition = "#result.projectId != null"),
            @CacheEvict(value = "projects", key = "'all'")
    })
    public TaskResponse createTask(TaskCreateRequest request) {
        log.debug("Creating new task with title: {}", request.getTitle());
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        task.setCreatedAt(LocalDateTime.now());
        task.setStatus(TaskStatus.TODO); // Default status for new tasks

        // Set project if provided
        if (request.getProjectId() != null) {
            projectRepository.findById(request.getProjectId())
                    .ifPresentOrElse(
                            task::setProject,
                            () -> log.warn("Project with id {} not found when creating task", request.getProjectId()));
        }

        Task savedTask = taskRepository.save(task);
        log.info("Task created successfully with id: {}", savedTask.getId());
        return convertToTaskResponse(savedTask);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "tasks", key = "#id"),
            @CacheEvict(value = "tasks", key = "'all'"),
            @CacheEvict(value = "tasks", key = "'project-' + #result.projectId", condition = "#result.projectId != null"),
            @CacheEvict(value = "projects", key = "#result.projectId", condition = "#result.projectId != null"),
            @CacheEvict(value = "projects", key = "'all'")
    })
    public TaskResponse updateTask(Long id, TaskUpdateRequest request) {
        log.debug("Updating task with id: {}", id);
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Task not found with id: " + id));

        // Update fields if provided
        if (request.getTitle() != null) {
            existingTask.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            existingTask.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            existingTask.setStatus(request.getStatus());
        }
        if (request.getPriority() != null) {
            existingTask.setPriority(request.getPriority());
        }
        if (request.getDueDate() != null) {
            existingTask.setDueDate(request.getDueDate());
        }

        // Update project if provided
        if (request.getProjectId() != null) {
            projectRepository.findById(request.getProjectId())
                    .ifPresent(existingTask::setProject);
        }

        Task updatedTask = taskRepository.save(existingTask);
        log.info("Task updated successfully with id: {}", updatedTask.getId());
        return convertToTaskResponse(updatedTask);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "tasks", key = "#id"),
            @CacheEvict(value = "tasks", key = "'all'"),
            @CacheEvict(value = "projects", key = "'all'")
    })
    public void deleteTask(Long id) {
        log.debug("Deleting task with id: {}", id);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Task not found with id: " + id));

        // Extract project ID for cache eviction
        Long projectId = Optional.ofNullable(task.getProject())
                .map(Project::getId)
                .orElse(null);

        if (projectId != null) {
            cacheManager.getCache("tasks").evict("project-" + projectId);
            cacheManager.getCache("projects").evict(projectId);
        }

        taskRepository.deleteById(id);
        log.info("Task deleted successfully with id: {}", id);
    }

    @Override
    @Cacheable(value = "tasks", key = "'project-' + #projectId")
    public List<TaskResponse> getTasksByProject(Long projectId) {
        log.debug("Fetching tasks for project id: {}", projectId);
        return taskRepository.findByProjectId(projectId).stream()
                .map(this::convertToTaskResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "tasks", key = "'status-' + #status")
    public List<TaskResponse> getTasksByStatus(TaskStatus status) {
        log.debug("Fetching tasks with status: {}", status);
        return taskRepository.findByStatus(status).stream()
                .map(this::convertToTaskResponse)
                .collect(Collectors.toList());
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
                Optional.ofNullable(task.getProject()).map(Project::getId).orElse(null),
                Optional.ofNullable(task.getProject()).map(Project::getName).orElse(null));
    }
}
