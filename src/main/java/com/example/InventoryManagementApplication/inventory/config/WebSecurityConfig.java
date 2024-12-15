package com.example.InventoryManagementApplication.inventory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class WebSecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        // In-memory users:
        // Admin user: username: admin, password: admin123, role: ADMIN
        // Normal user: username: user, password: user123, role: USER
        return new InMemoryUserDetailsManager(
                User.withUsername("admin")
                        .password("{noop}admin123") // {noop} means no password encoder
                        .roles("ADMIN")
                        .build(),
                User.withUsername("user")
                        .password("{noop}user123")
                        .roles("USER")
                        .build()
        );
    }
}
