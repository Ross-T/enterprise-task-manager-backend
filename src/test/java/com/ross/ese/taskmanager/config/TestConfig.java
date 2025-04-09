package com.ross.ese.taskmanager.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import com.ross.ese.taskmanager.service.SupabaseAuthService;
import org.mockito.Mockito;

@TestConfiguration
@Profile("test")
public class TestConfig {
    
    /**
     * Create a mock SupabaseAuthService to avoid Supabase dependency during tests
     */
    @Bean
    @Primary
    SupabaseAuthService supabaseAuthService() {
        return Mockito.mock(SupabaseAuthService.class);
    }
    
}
