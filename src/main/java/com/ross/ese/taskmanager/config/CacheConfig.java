package com.ross.ese.taskmanager.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for application caching.
 * Uses in-memory cache for frequently accessed data.
 */
@Configuration
@EnableCaching
public class CacheConfig {
    
    /**
     * Configures the cache manager with named caches
     * 
     * @return The configured cache manager
     */
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("tasks", "projects", "users");
    }
}
