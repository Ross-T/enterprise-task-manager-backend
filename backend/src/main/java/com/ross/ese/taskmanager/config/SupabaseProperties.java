package com.ross.ese.taskmanager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;

/**
 * Configuration properties for Supabase integration.
 * This class maps the custom supabase properties from application.properties.
 */
@Configuration
@ConfigurationProperties(prefix = "supabase")
@Getter
@Setter
public class SupabaseProperties {
    private String url;
    private String key;
}