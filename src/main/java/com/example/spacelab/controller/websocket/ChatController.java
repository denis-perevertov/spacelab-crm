package com.example.spacelab.controller.websocket;

import com.example.spacelab.dto.chat.ChatGroupSaveDTO;
import com.example.spacelab.dto.chat.ChatTypingUser;
import com.example.spacelab.dto.chat.ClientChatMessage;
import com.example.spacelab.dto.chat.ServerChatMessage;
import com.example.spacelab.exception.ObjectValidationException;
import com.example.spacelab.mapper.ChatMapper;
import com.example.spacelab.service.ChatService;
import com.example.spacelab.validator.ChatValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;
    private final ChatMapper chatMapper;
    private final ChatValidator validator;

    @MessageMapping("/chat/{chatId}")
    @SendTo("/topic/chat/{chatId}")
    public ServerChatMessage publishMessage(@DestinationVariable Long chatId, @Payload ClientChatMessage clientMsg) {
        ServerChatMessage msg = chatService.saveChatMessage(clientMsg);
        log.info("Received msg: {}", msg);
        return msg;
    }

    @MessageMapping("/chat/{chatId}/typing")
    @SendTo("/topic/chat/{chatId}/typing")
    public ChatTypingUser userIsTyping(@DestinationVariable Long chatId, @Payload ChatTypingUser user) {
        return user;
    }

    @MessageMapping("/user/{userId}/inbox")
    @SendTo("/queue/user/{userId}")
    public ServerChatMessage sendPrivateMessage(@DestinationVariable Long userId, @Payload ClientChatMessage clientMsg) {
        ServerChatMessage msg = chatService.savePrivateMessage(clientMsg);
        log.info("Received private msg: {}", msg);
        return msg;
    }

    @GetMapping
    public ResponseEntity<?> getChatGroups(@RequestParam(required = false) String query) {
        return ResponseEntity.ok(chatService.getChatsForLoggedInAdmin(query));
    }

    @PostMapping("/groups")
    public ResponseEntity<?> saveChatGroup(@ModelAttribute ChatGroupSaveDTO dto,
                                           BindingResult bindingResult) {
        validator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ObjectValidationException(errors);
        }
        return ResponseEntity.ok(chatMapper.fromChatToDTO(chatService.saveChatGroup(dto)));
    }
}
