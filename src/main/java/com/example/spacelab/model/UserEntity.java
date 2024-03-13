package com.example.spacelab.model;

import com.example.spacelab.model.chat.Chat;
import com.example.spacelab.model.role.UserRole;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String avatar;

    private String userEntityName;

    @ManyToMany
    private List<Chat> chats;

    @ManyToOne
    private UserRole role;
}
