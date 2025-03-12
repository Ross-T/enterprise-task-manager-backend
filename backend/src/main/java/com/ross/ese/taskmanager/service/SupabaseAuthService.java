package com.ross.ese.taskmanager.service;

public interface SupabaseAuthService {
    /**
     * Registers a new user with Supabase Auth
     * 
     * @param email User's email
     * @param password User's password
     * @param username User's username
     * @return JWT token if successful, null otherwise
     */
    String signUp(String email, String password, String username);
    
    /**
     * Authenticates a user with Supabase Auth
     *
     * @param email User's email
     * @param password User's password
     * @return JWT token if successful, null otherwise
     */
    String signIn(String email, String password);
    
    /**
     * Verifies if a JWT token is valid
     *
     * @param token JWT token to verify
     * @return true if valid, false otherwise
     */
    boolean verifyToken(String token);
}