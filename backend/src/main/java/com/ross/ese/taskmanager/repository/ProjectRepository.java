package com.ross.ese.taskmanager.repository;

import com.ross.ese.taskmanager.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing Project entities.
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    /**
     * Find projects by owner ID with pagination
     * @param ownerId The owner's ID
     * @param pageable Pagination information
     * @return Page of projects owned by the specified user
     */
    Page<Project> findByOwnerId(Long ownerId, Pageable pageable);
    
    /**
     * Find projects where a user is a member with pagination
     * @param userId The user's ID
     * @param pageable Pagination information
     * @return Page of projects where the user is a member
     */
    @Query("SELECT p FROM Project p JOIN p.members m WHERE m.id = :userId")
    Page<Project> findProjectsByMemberId(@Param("userId") Long userId, Pageable pageable);
}