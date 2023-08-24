package com.example.spacelab.model;

import jakarta.persistence.*;
import lombok.Data;

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

    @OneToMany(mappedBy = "mentor")
    private List<Course> coursesAsMentor;

    @OneToMany(mappedBy = "manager")
    private List<Course> coursesAsManager;

    @OneToMany(mappedBy = "mentor")
    private List<Lesson> lessonsAsMentor;

    @OneToMany(mappedBy = "manager")
    private List<Lesson> lessonsaAsManager;
}
