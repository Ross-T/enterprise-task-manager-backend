package com.ross.ese.taskmanager.service;

import com.ross.ese.taskmanager.dto.ProjectCreateRequest;
import com.ross.ese.taskmanager.dto.ProjectResponse;
import com.ross.ese.taskmanager.dto.ProjectUpdateRequest;

import java.util.List;

public interface ProjectService {
    List<ProjectResponse> getAllProjects();
    ProjectResponse getProjectById(Long id);
    ProjectResponse createProject(ProjectCreateRequest request);
    ProjectResponse updateProject(Long id, ProjectUpdateRequest request);
    void deleteProject(Long id);
}
