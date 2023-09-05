package com.example.spacelab.config;

import com.example.spacelab.model.admin.Admin;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public class CustomInMemoryUserDetailsManager implements UserDetailsManager {

    private final Map<String, Admin> users = new HashMap<>();

    public CustomInMemoryUserDetailsManager() {}

    public CustomInMemoryUserDetailsManager(UserDetails... users) {
        for(UserDetails user : users) createUser(user);
    }

    @Override
    public void createUser(UserDetails user) {
        if(!userExists(user.getUsername())) users.put(user.getUsername().toLowerCase(), (Admin) user);
    }

    @Override
    public void updateUser(UserDetails user) {
        if(userExists(user.getUsername())) users.put(user.getUsername().toLowerCase(), (Admin) user);
    }

    @Override
    public void deleteUser(String username) {
        users.remove(username.toLowerCase());
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean userExists(String username) {
        return users.containsKey(username.toLowerCase());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin user = users.get(username.toLowerCase());

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return user;
    }
}
