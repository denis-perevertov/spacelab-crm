package com.example.spacelab.model;

import com.example.spacelab.model.role.PermissionSet;
import jakarta.persistence.*;
import lombok.Data;

import java.security.Permission;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name="roles")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany
    private List<UserEntity> userList;

    @Embedded
    private PermissionSet permissions;
}
