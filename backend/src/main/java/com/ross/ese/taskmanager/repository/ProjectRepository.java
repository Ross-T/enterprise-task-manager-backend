package com.ross.ese.taskmanager.repository;

import com.ross.ese.taskmanager.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing Project entities.
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Using only built-in methods from JpaRepository
}