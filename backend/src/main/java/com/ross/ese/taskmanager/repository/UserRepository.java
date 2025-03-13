package com.ross.ese.taskmanager.repository;

import com.ross.ese.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for accessing User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Find a user by username
     * @param username The username to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Check if a username already exists
     * @param username The username to check
     * @return true if the username exists, false otherwise
     */
    Boolean existsByUsername(String username);
    
    /**
     * Check if an email already exists
     * @param email The email to check
     * @return true if the email exists, false otherwise
     */
    Boolean existsByEmail(String email);
}