package com.example.spacelab.controller.websocket;

import com.example.spacelab.dto.chat.ChatGroupSaveDTO;
import com.example.spacelab.dto.chat.ChatTypingUser;
import com.example.spacelab.dto.chat.ClientChatMessage;
import com.example.spacelab.dto.chat.ServerChatMessage;
import com.example.spacelab.exception.ObjectValidationException;
import com.example.spacelab.mapper.ChatMapper;
import com.example.spacelab.service.ChatService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;
    private final ChatMapper chatMapper;

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

//    @MessageMapping("/messages/{msgId}/status")
//    @SendTo("/topic/")

    @GetMapping("/chat")
    public ResponseEntity<?> getChatGroups() {
        return ResponseEntity.ok(chatService.getChatsForLoggedInAdmin().stream().map(chatMapper::fromChatToDTO).toList());
    }

    @PostMapping("/chat/groups")
    public ResponseEntity<?> saveChatGroup(@ModelAttribute @Valid ChatGroupSaveDTO dto,
                                           BindingResult bindingResult) {
        log.info("dto: {}", dto);
        log.info(Arrays.toString(dto.admins()));
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ObjectValidationException(errors);
        }
        chatService.saveChat(dto);
        return ResponseEntity.ok().build();
    }
}
