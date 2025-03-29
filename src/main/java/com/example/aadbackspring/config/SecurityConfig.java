package com.example.aadbackspring.config;

import com.example.aadbackspring.exception.CustomAccessDeniedHandler;
import com.example.aadbackspring.exception.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF protection for APIs (configure as needed)
                .csrf(csrf -> csrf.disable())

                // Configure exception handling for both authentication and authorization errors
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(customAuthenticationEntryPoint)  // for unauthenticated requests
                        .accessDeniedHandler(customAccessDeniedHandler)            // for forbidden (authorization) errors
                )

                // Define URL access rules
                .authorizeHttpRequests(authz -> authz
                        // Permit all users to access the /stripe endpoints
                        .requestMatchers("/stripe/**").permitAll()
                        // Allow access to the favicon.ico
                        .requestMatchers("/favicon.ico").permitAll()
                        // Allow access to authentication endpoints (e.g., /auth/**)
                        .requestMatchers("/auth/**").permitAll()
                        // Restrict access to the /subscriptions endpoint to only admins
                        .requestMatchers("/subscriptions/**").hasRole("admin")
                        // Allow GET requests for other public resources
                        .requestMatchers(HttpMethod.GET,
                                "/articles/**", "/categories/**", "/coins/**", "/dexchanges/**", "/news/**", "/terms/**", "/users/**").permitAll()
                        // Restrict POST, PUT, DELETE methods for certain endpoints to admins
                        .requestMatchers(HttpMethod.POST,
                                "/articles/**", "/categories/**", "/coins/**", "/dexchanges/**", "/news/**", "/terms/**", "/users/**").hasRole("admin")
                        .requestMatchers(HttpMethod.PUT,
                                "/articles/**", "/categories/**", "/coins/**", "/dexchanges/**", "/news/**", "/terms/**", "/users/**").hasRole("admin")
                        .requestMatchers(HttpMethod.DELETE,
                                "/articles/**", "/categories/**", "/coins/**", "/dexchanges/**", "/news/**", "/terms/**", "/users/**").hasRole("admin")
                        // Require authentication for all other requests
                        .anyRequest().authenticated()
                        
                );

        // Add the JWT authentication filter before the default UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
