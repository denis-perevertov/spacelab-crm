package com.example.spacelab.config;

import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.role.UserRole;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    public UserDetails testUser() {
        Admin basicUser = new Admin();
        basicUser.setFirstName("test");
        basicUser.setEmail("test@gmail.com");
        basicUser.setPassword("test");

        UserRole adminRole = new UserRole();
        adminRole.setName("TEST_ROLE");

        basicUser.setRole(adminRole);

        return basicUser;
    }

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return new CustomInMemoryUserDetailsManager(testUser());
    }

}
