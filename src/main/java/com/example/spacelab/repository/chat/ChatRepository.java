package com.example.spacelab.repository.chat;

import com.example.spacelab.model.chat.Chat;
import com.example.spacelab.model.chat.GroupChat;
import com.example.spacelab.model.chat.PrivateChat;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Hidden
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("""
            SELECT c
            FROM Chat c
            JOIN c.members mem
            WHERE mem.id = :userId
            """)
    List<Chat> findGroupChatsForUser(Long userId);

    @Query("""
            SELECT c
            FROM Chat c
            JOIN c.members mem
            WHERE mem.id = :userId
            AND LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))
            """)
    List<Chat> findGroupChatsForUser(Long userId, String query);

    @Query("""
            SELECT c
            FROM PrivateChat c
            WHERE c.person1.id = :userId
            OR c.person2.id = :userId
            """)
    List<Chat> findPrivateChatsForUser(Long userId);



}
