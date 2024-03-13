package com.example.spacelab.model.chat;

import com.example.spacelab.model.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "chats")
@Inheritance
@DiscriminatorColumn(name = "chat_type", discriminatorType = DiscriminatorType.STRING)
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String icon;

    @OneToMany(mappedBy = "chat")
    private List<ChatMessage> messages = new ArrayList<>();

    @Setter(AccessLevel.NONE)
    @Enumerated(EnumType.STRING)
    @Column(name = "chat_type", insertable = false, updatable = false)
    private ChatType type;

}
