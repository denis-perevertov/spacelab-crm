package com.example.spacelab.model;

import com.example.spacelab.model.role.UserRole;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String avatar;

    @ManyToOne
    private UserRole role;
}
