package com.ross.ese.taskmanager.repository;

import com.ross.ese.taskmanager.model.Task;
import com.ross.ese.taskmanager.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for accessing Task entities.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    /**
     * Find tasks assigned to a specific user with pagination
     * @param userId The assignee's ID
     * @param pageable Pagination information
     * @return Page of tasks assigned to the specified user
     */
    Page<Task> findByAssigneeId(Long userId, Pageable pageable);
    
    /**
     * Find tasks belonging to a specific project with pagination
     * @param projectId The project's ID
     * @param pageable Pagination information
     * @return Page of tasks in the specified project
     */
    Page<Task> findByProjectId(Long projectId, Pageable pageable);
    
    /**
     * Find tasks with a specific status
     * @param status The status to filter by
     * @return List of tasks with the specified status
     */
    List<Task> findByStatus(TaskStatus status);
}