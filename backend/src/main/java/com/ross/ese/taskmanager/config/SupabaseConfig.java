package com.ross.ese.taskmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration for Supabase REST API integration.
 * This creates a WebClient configured for Supabase authentication calls.
 */
@Configuration
public class SupabaseConfig {

    private final SupabaseProperties supabaseProperties;
    
    public SupabaseConfig(SupabaseProperties supabaseProperties) {
        this.supabaseProperties = supabaseProperties;
    }

    @Bean
    public WebClient supabaseAuthClient() {
        return WebClient.builder()
            .baseUrl(supabaseProperties.getUrl() + "/auth/v1")
            .defaultHeader("apikey", supabaseProperties.getKey())
            .defaultHeader("Content-Type", "application/json")
            .build();
    }
}