package com.example.spacelab.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name="admins")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;

    private String phone;
    private String email;

    private String password;
    @Transient private String confirmPassword;

    @ManyToOne
    private UserRole role;

    @ManyToMany
    private List<Course> courses;
}
