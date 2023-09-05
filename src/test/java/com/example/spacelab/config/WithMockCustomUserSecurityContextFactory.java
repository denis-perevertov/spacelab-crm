package com.example.spacelab.config;

import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.role.UserRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {

        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Admin basicUser = new Admin();
        basicUser.setFirstName("test");
        basicUser.setEmail("test@gmail.com");
        basicUser.setPassword("test");

        UserRole adminRole = new UserRole();
        adminRole.setName("TEST_ROLE");

        basicUser.setRole(adminRole);

        Authentication auth =
                new UsernamePasswordAuthenticationToken(basicUser, "password", basicUser.getAuthorities());

        context.setAuthentication(auth);
        return context;
    }
}
