package com.ross.ese.taskmanager.service.impl;

import com.ross.ese.taskmanager.dto.ProjectCreateRequest;
import com.ross.ese.taskmanager.dto.ProjectResponse;
import com.ross.ese.taskmanager.dto.ProjectUpdateRequest;
import com.ross.ese.taskmanager.model.Project;
import com.ross.ese.taskmanager.repository.ProjectRepository;
import com.ross.ese.taskmanager.repository.TaskRepository;
import com.ross.ese.taskmanager.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProjectServiceImpl implements ProjectService {
    
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    
    @Override
    public List<ProjectResponse> getAllProjects() {
        log.debug("Fetching all projects");
        return projectRepository.findAll().stream()
                .map(this::convertToProjectResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public ProjectResponse getProjectById(Long id) {
        log.debug("Fetching project with id: {}", id);
        return projectRepository.findById(id)
                .map(this::convertToProjectResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    "Project not found with id: " + id));
    }
    
    @Override
    public ProjectResponse createProject(ProjectCreateRequest request) {
        log.debug("Creating new project with name: {}", request.getName());
        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setCreatedAt(LocalDateTime.now());
        
        Project savedProject = projectRepository.save(project);
        log.info("Project created successfully with id: {}", savedProject.getId());
        return convertToProjectResponse(savedProject);
    }
    
    @Override
    public ProjectResponse updateProject(Long id, ProjectUpdateRequest request) {
        log.debug("Updating project with id: {}", id);
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                    "Project not found with id: " + id));
        
        // Update fields if provided
        if (request.getName() != null) existingProject.setName(request.getName());
        if (request.getDescription() != null) existingProject.setDescription(request.getDescription());
        
        Project updatedProject = projectRepository.save(existingProject);
        log.info("Project updated successfully with id: {}", updatedProject.getId());
        return convertToProjectResponse(updatedProject);
    }
    
    @Override
    public void deleteProject(Long id) {
        log.debug("Deleting project with id: {}", id);
        if (!projectRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "Project not found with id: " + id);
        }
        
        projectRepository.deleteById(id);
        log.info("Project deleted successfully with id: {}", id);
    }
    
    private ProjectResponse convertToProjectResponse(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getCreatedAt(),
                taskRepository.findByProjectId(project.getId()).size()
        );
    }
}
