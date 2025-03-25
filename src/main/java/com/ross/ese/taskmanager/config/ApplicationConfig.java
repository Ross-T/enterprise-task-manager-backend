package com.ross.ese.taskmanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Centralized configuration manager for application properties.
 * This provides a single point of access for all configuration values.
 */
@Component
public class ApplicationConfig {
    
    @Value("${app.auth.dev-mode:false}")
    private boolean devMode;
    
    /**
     * Determines if the application is running in development mode
     * where certain security checks are bypassed.
     *
     * @return true if running in development mode, false otherwise
     */
    public boolean isDevMode() {
        return devMode;
    }
}