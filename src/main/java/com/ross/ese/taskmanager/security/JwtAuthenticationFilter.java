package com.ross.ese.taskmanager.security;

import com.ross.ese.taskmanager.service.SupabaseAuthService;
import com.ross.ese.taskmanager.config.ApplicationConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final SupabaseAuthService supabaseAuthService;
    private final ApplicationConfig appConfig;
    
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response, 
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
            
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            
            log.debug("Processing token: {}", token.substring(0, Math.min(token.length(), 10)) + "...");
            
            // Check if it's a development token
            boolean authenticated = false;
            if (appConfig.isDevMode() && token.startsWith("dev-login-")) {
                log.debug("Authenticating with development token");
                authenticated = true;
                
                // Set authentication in context
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(
                        "dev-user@example.com",
                        null,
                        authorities
                    );
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                // Regular token verification
                log.debug("Verifying token with Supabase");
                authenticated = supabaseAuthService.verifyToken(token);
                if (authenticated) {
                    UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(
                            "user",
                            null,
                            null
                        );
                    
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        
        filterChain.doFilter(request, response);
    }
}