package com.ross.ese.taskmanager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for Supabase integration.
 * This class maps the custom supabase properties from application.properties.
 */
@Configuration
@ConfigurationProperties(prefix = "supabase")
public class SupabaseProperties {
    
    private String url;
    private String key;
    
    // Getters and setters
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
}