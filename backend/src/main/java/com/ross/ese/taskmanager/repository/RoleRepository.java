package com.ross.ese.taskmanager.repository;

import com.ross.ese.taskmanager.model.ERole;
import com.ross.ese.taskmanager.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for accessing Role entities.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    /**
     * Find a role by name
     * @param name The role name to search for
     * @return Optional containing the role if found
     */
    Optional<Role> findByName(ERole name);
}