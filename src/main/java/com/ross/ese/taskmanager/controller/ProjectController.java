package com.ross.ese.taskmanager.controller;

import com.ross.ese.taskmanager.dto.ProjectCreateRequest;
import com.ross.ese.taskmanager.dto.ProjectResponse;
import com.ross.ese.taskmanager.dto.ProjectUpdateRequest;
import com.ross.ese.taskmanager.model.Project;
import com.ross.ese.taskmanager.repository.ProjectRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
public class ProjectController {
    
    private final ProjectRepository projectRepository;
    
    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }
    
    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        List<ProjectResponse> responses = projects.stream()
            .map(this::convertToProjectResponse)
            .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id) {
        return projectRepository.findById(id)
                .map(this::convertToProjectResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectCreateRequest request) {
        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setCreatedAt(LocalDateTime.now());
        
        Project savedProject = projectRepository.save(project);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertToProjectResponse(savedProject));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectUpdateRequest request) {
        
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        
        if (request.getName() != null) existingProject.setName(request.getName());
        if (request.getDescription() != null) existingProject.setDescription(request.getDescription());
        
        Project updatedProject = projectRepository.save(existingProject);
        return ResponseEntity.ok(convertToProjectResponse(updatedProject));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        if (!projectRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        projectRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    private ProjectResponse convertToProjectResponse(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getCreatedAt()
        );
    }
}