package com.ross.ese.taskmanager.service;

import com.ross.ese.taskmanager.dto.TaskCreateRequest;
import com.ross.ese.taskmanager.dto.TaskResponse;
import com.ross.ese.taskmanager.dto.TaskUpdateRequest;
import com.ross.ese.taskmanager.model.Project;
import com.ross.ese.taskmanager.model.Task;
import com.ross.ese.taskmanager.model.TaskPriority;
import com.ross.ese.taskmanager.model.TaskStatus;
import com.ross.ese.taskmanager.repository.ProjectRepository;
import com.ross.ese.taskmanager.repository.TaskRepository;
import com.ross.ese.taskmanager.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task1;
    private Task task2;
    private Project project;

    @BeforeEach
    public void setup() {
        project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setCreatedAt(LocalDateTime.now());

        task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Test Task 1");
        task1.setDescription("Test Description 1");
        task1.setStatus(TaskStatus.TODO);
        task1.setPriority(TaskPriority.MEDIUM);
        task1.setCreatedAt(LocalDateTime.now());
        task1.setProject(project);

        task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Test Task 2");
        task2.setDescription("Test Description 2");
        task2.setStatus(TaskStatus.IN_PROGRESS);
        task2.setPriority(TaskPriority.HIGH);
        task2.setCreatedAt(LocalDateTime.now());
    }

    @Test
    public void testGetAllTasks() {
        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<TaskResponse> result = taskService.getAllTasks();

        assertEquals(2, result.size());
        assertEquals("Test Task 1", result.get(0).getTitle());
        assertEquals("Test Task 2", result.get(1).getTitle());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void testGetTaskById_Success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));

        TaskResponse result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals("Test Task 1", result.getTitle());
        assertEquals(TaskStatus.TODO, result.getStatus());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetTaskById_NotFound() {
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> taskService.getTaskById(999L));
        verify(taskRepository, times(1)).findById(999L);
    }

    @Test
    public void testCreateTask_WithoutProject() {
        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle("New Task");
        request.setDescription("New Description");
        request.setPriority(TaskPriority.LOW);

        Task savedTask = new Task();
        savedTask.setId(3L);
        savedTask.setTitle("New Task");
        savedTask.setDescription("New Description");
        savedTask.setPriority(TaskPriority.LOW);
        savedTask.setStatus(TaskStatus.TODO);
        savedTask.setCreatedAt(LocalDateTime.now());

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        TaskResponse result = taskService.createTask(request);

        assertNotNull(result);
        assertEquals("New Task", result.getTitle());
        assertEquals(TaskStatus.TODO, result.getStatus());
        verify(taskRepository, times(1)).save(any(Task.class));
        verify(projectRepository, never()).findById(any());
    }

    @Test
    public void testCreateTask_WithProject() {
        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle("New Task");
        request.setDescription("New Description");
        request.setPriority(TaskPriority.LOW);
        request.setProjectId(1L);

        Task savedTask = new Task();
        savedTask.setId(3L);
        savedTask.setTitle("New Task");
        savedTask.setDescription("New Description");
        savedTask.setPriority(TaskPriority.LOW);
        savedTask.setStatus(TaskStatus.TODO);
        savedTask.setCreatedAt(LocalDateTime.now());
        savedTask.setProject(project);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        TaskResponse result = taskService.createTask(request);

        assertNotNull(result);
        assertEquals("New Task", result.getTitle());
        assertEquals(TaskStatus.TODO, result.getStatus());
        assertEquals(1L, result.getProjectId());
        assertEquals("Test Project", result.getProjectName());
        verify(taskRepository, times(1)).save(any(Task.class));
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateTask_Success() {
        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setTitle("Updated Title");
        request.setStatus(TaskStatus.IN_PROGRESS);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));
        
        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle("Updated Title");
        updatedTask.setDescription("Test Description 1");
        updatedTask.setStatus(TaskStatus.IN_PROGRESS);
        updatedTask.setPriority(TaskPriority.MEDIUM);
        updatedTask.setCreatedAt(task1.getCreatedAt());
        updatedTask.setProject(project);
        
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        TaskResponse result = taskService.updateTask(1L, request);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals(TaskStatus.IN_PROGRESS, result.getStatus());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void testUpdateTask_NotFound() {
        TaskUpdateRequest request = new TaskUpdateRequest();
        request.setTitle("Updated Title");

        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> taskService.updateTask(999L, request));
        verify(taskRepository, times(1)).findById(999L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    public void testDeleteTask_Success() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteTask_NotFound() {
        when(taskRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> taskService.deleteTask(999L));
        verify(taskRepository, times(1)).existsById(999L);
        verify(taskRepository, never()).deleteById(999L);
    }

    @Test
    public void testGetTasksByProject_Success() {
        when(projectRepository.existsById(1L)).thenReturn(true);
        when(taskRepository.findByProjectId(1L)).thenReturn(List.of(task1));

        List<TaskResponse> result = taskService.getTasksByProject(1L);

        assertEquals(1, result.size());
        assertEquals("Test Task 1", result.get(0).getTitle());
        verify(projectRepository, times(1)).existsById(1L);
        verify(taskRepository, times(1)).findByProjectId(1L);
    }

    @Test
    public void testGetTasksByProject_ProjectNotFound() {
        when(projectRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> taskService.getTasksByProject(999L));
        verify(projectRepository, times(1)).existsById(999L);
        verify(taskRepository, never()).findByProjectId(anyLong());
    }

    @Test
    public void testGetTasksByStatus() {
        when(taskRepository.findByStatus(TaskStatus.TODO)).thenReturn(List.of(task1));

        List<TaskResponse> result = taskService.getTasksByStatus(TaskStatus.TODO);

        assertEquals(1, result.size());
        assertEquals("Test Task 1", result.get(0).getTitle());
        assertEquals(TaskStatus.TODO, result.get(0).getStatus());
        verify(taskRepository, times(1)).findByStatus(TaskStatus.TODO);
    }
}
