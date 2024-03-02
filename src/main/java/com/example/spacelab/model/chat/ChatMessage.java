package com.example.spacelab.model.chat;

import com.example.spacelab.model.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "chat_messages")
@Accessors(chain = true)
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 4096)
    private String content;

    private ZonedDateTime datetime;

    @ManyToOne
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private UserEntity sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private UserEntity recipient;

    @Enumerated(EnumType.STRING)
    private ChatMessageStatus status = ChatMessageStatus.CREATED;
}
