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

    private String first_name;
    private String last_name;

    private String phone;
    private String email;

    private String password;

    @ManyToOne
    private UserRole role;

    @ManyToMany
    private List<Course> courses;
}
