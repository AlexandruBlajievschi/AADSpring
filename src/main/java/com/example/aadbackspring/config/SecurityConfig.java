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
                        // Public endpoints for everyone:
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/favicon.ico").permitAll()
                        .requestMatchers("/auth/**").permitAll()

                        // Endpoints that require authentication (but not necessarily admin):
                        .requestMatchers("/stripe/**").authenticated()
                        .requestMatchers(HttpMethod.GET,
                                "/articles/**", "/categories/**", "/coins/**", "/dexchanges/**", "/news/**", "/terms/**", "/users/**")
                        .authenticated()

                        // Endpoints that require admin privileges (all non-GET methods):
                        .requestMatchers(HttpMethod.POST,
                                "/articles/**", "/categories/**", "/coins/**", "/dexchanges/**", "/news/**", "/terms/**", "/users/**")
                        .hasRole("admin")
                        .requestMatchers(HttpMethod.PUT,
                                "/articles/**", "/categories/**", "/coins/**", "/dexchanges/**", "/news/**", "/terms/**", "/users/**")
                        .hasRole("admin")
                        .requestMatchers(HttpMethod.DELETE,
                                "/articles/**", "/categories/**", "/coins/**", "/dexchanges/**", "/news/**", "/terms/**", "/users/**")
                        .hasRole("admin")

                        // Other admin-only endpoints:
                        .requestMatchers("/subscriptions/**").hasRole("admin")
                        .requestMatchers(HttpMethod.GET, "/api/config").hasRole("admin")
                        .requestMatchers(HttpMethod.PUT, "/api/config").hasRole("admin")

                        // All other requests require authentication
                        .anyRequest().authenticated()
                );

        // Add the JWT authentication filter before the default UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
