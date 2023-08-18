package com.example.spacelab.model;

import com.example.spacelab.model.dto.UserRoleDTO;
import jakarta.persistence.*;

@MappedSuperclass
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String avatar;

    @ManyToOne
    private UserRole role;
}
