package com.example.spacelab.model.notification;

import com.example.spacelab.model.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
//@Table(name = "notifications")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private UserEntity recipient;

    @Enumerated(EnumType.STRING)
    private NotificationEventType eventType;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

}
