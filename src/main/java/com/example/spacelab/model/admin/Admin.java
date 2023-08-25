package com.example.spacelab.model.admin;

import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.UserEntity;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

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

    @ManyToMany
    private List<Course> courses = new ArrayList<>();
}
