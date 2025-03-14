package com.ross.ese.taskmanager.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration for Supabase client integration.
 */
@Configuration
@RequiredArgsConstructor
public class SupabaseConfig {
    
    private final SupabaseProperties supabaseProperties;
    
    @Bean
    public WebClient supabaseAuthClient() {
        return WebClient.builder()
                .baseUrl(supabaseProperties.getUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("apikey", supabaseProperties.getKey())
                .build();
    }
}