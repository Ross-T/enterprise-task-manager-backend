package com.ross.ese.taskmanager.repository;

import com.ross.ese.taskmanager.model.Task;
import com.ross.ese.taskmanager.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for accessing Task entities.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    /**
     * Find tasks assigned to a specific user
     * 
     * @param userId The assignee's ID
     * @return List of tasks assigned to the specified user
     */
    List<Task> findByAssigneeId(Long userId);

    /**
     * Find tasks belonging to a specific project
     * 
     * @param projectId The project's ID
     * @return List of tasks in the specified project
     */
    List<Task> findByProjectId(Long projectId);

    /**
     * Find tasks with a specific status
     * 
     * @param status The status to filter by
     * @return List of tasks with the specified status
     */
    List<Task> findByStatus(TaskStatus status);

    /**
     * Count tasks belonging to a specific project
     * 
     * @param projectId The project's ID
     * @return The number of tasks in the specified project
     */
    Integer countByProjectId(Long projectId);
}
