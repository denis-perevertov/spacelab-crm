package com.example.spacelab.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="admins")
public class Admin extends UserEntity {

    private String firstName;
    private String lastName;

    private String phone;
    private String email;

    private String password;
    @Transient private String confirmPassword;

    @ManyToMany
    private List<Course> courses = new ArrayList<>();
}
