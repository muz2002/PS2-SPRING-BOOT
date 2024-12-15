package com.example.InventoryManagementApplication.inventory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Swagger and API docs open to all
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/api-docs/**").permitAll()

                        // Allow GET requests on products for both ADMIN and USER
                        .requestMatchers(HttpMethod.GET, "/api/products/**").hasAnyRole("ADMIN","USER")
                        // Non-GET requests on products restricted to ADMIN
                        .requestMatchers("/api/products/**").hasRole("ADMIN")
                        // Categories and Inventory require ADMIN
                        .requestMatchers("/api/categories/**").hasRole("ADMIN")
                        .requestMatchers("/api/inventory/**").hasRole("ADMIN")

                        // Any other request requires authentication
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
