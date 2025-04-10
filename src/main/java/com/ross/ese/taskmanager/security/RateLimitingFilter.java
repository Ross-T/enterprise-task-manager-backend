package com.ross.ese.taskmanager.security;

import com.google.common.util.concurrent.RateLimiter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Filter that implements rate limiting per IP address to prevent brute force attacks and DoS attempts.
 * Uses Google Guava's RateLimiter to control request rates.
 */
@Component
@Slf4j
public class RateLimitingFilter extends OncePerRequestFilter {
    
    private static class RateLimiterEntry {
        final RateLimiter limiter;
        Instant lastAccessed;
        
        RateLimiterEntry(RateLimiter limiter) {
            this.limiter = limiter;
            this.lastAccessed = Instant.now();
        }
        
        void updateLastAccessed() {
            this.lastAccessed = Instant.now();
        }
    }
    
    private final Map<String, RateLimiterEntry> limiterEntries = new ConcurrentHashMap<>();
    
    @Value("${app.rate-limiting.requests-per-second:5.0}")
    private double requestsPerSecond;
    
    @Value("${app.rate-limiting.burst-size:10}")
    private int burstSize;
    
    @Value("${app.rate-limiting.enabled:true}")
    private boolean rateLimitingEnabled;
    
    @Value("${app.rate-limiting.expiration-minutes:30}")
    private int expirationMinutes;
    
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response, 
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        // Skip rate limiting if disabled or for non-auth endpoints
        String path = request.getRequestURI();
        if (!rateLimitingEnabled || !isRateLimitedEndpoint(path)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String clientIp = getClientIp(request);
        
        RateLimiterEntry entry = limiterEntries.computeIfAbsent(clientIp, 
            k -> new RateLimiterEntry(RateLimiter.create(requestsPerSecond, burstSize, TimeUnit.SECONDS)));
        
        entry.updateLastAccessed();
        
        if (entry.limiter.tryAcquire()) {
            filterChain.doFilter(request, response);
        } else {
            // Rate limit exceeded
            log.warn("Rate limit exceeded for IP: {}", clientIp);
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many requests. Please try again later.");
            
            response.setHeader("Retry-After", "60");  // seconds
        }
    }
    
    /**
     * Hourly scheduled task to clean up expired rate limiters
     */
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void scheduledCleanup() {
        cleanupOldLimiters();
    }
    
    /**
     * Rate-limmited endpoints
     */
    private boolean isRateLimitedEndpoint(String path) {
        return (path.contains("/api/auth/") && !path.contains("/api/auth/verify")) || 
               path.contains("/api/users/") ||
               path.contains("/api/admin/");
    }
    
    /**
     * Extract client IP address from request
     */
    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0].trim();
    }
    
    /**
     * Remove limiters that haven't been used recently to prevent memory leaks
     */
    private void cleanupOldLimiters() {
        log.debug("Performing rate limiter cleanup");
        
        Instant expirationTime = Instant.now().minus(expirationMinutes, ChronoUnit.MINUTES);
        int beforeSize = limiterEntries.size();
        
        limiterEntries.entrySet().removeIf(entry -> 
            entry.getValue().lastAccessed.isBefore(expirationTime));
        
        int removedCount = beforeSize - limiterEntries.size();
        if (removedCount > 0) {
            log.info("Removed {} expired rate limiters. Current size: {}", 
                     removedCount, limiterEntries.size());
        }
    }
}
