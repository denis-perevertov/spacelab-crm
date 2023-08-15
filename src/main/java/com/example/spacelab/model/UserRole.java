package com.example.spacelab.model;

import jakarta.persistence.*;
import lombok.Data;

import java.security.Permission;
import java.util.Set;

@Data
@Entity
@Table(name="roles")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ElementCollection
    private Set<String> permissions;
}
