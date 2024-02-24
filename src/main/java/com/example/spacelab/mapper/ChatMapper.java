package com.example.spacelab.mapper;

import com.example.spacelab.dto.chat.ChatGroupSaveDTO;
import com.example.spacelab.dto.chat.ChatListDTO;
import com.example.spacelab.dto.chat.ChatMemberDTO;
import com.example.spacelab.dto.chat.ChatMessageDTO;
import com.example.spacelab.model.UserEntity;
import com.example.spacelab.model.chat.Chat;
import com.example.spacelab.model.chat.ChatMessage;
import com.example.spacelab.repository.chat.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatMapper {

    private ChatRepository chatRepository;
//
//    public Chat fromSaveDTOToChat(ChatGroupSaveDTO dto) {
//        Chat chat = dto.id() != null ? chatRepository.getReferenceById(dto.id()) : new Chat();
//        chat.setName(dto.name());
//        chat.get
//    }

    public ChatListDTO fromChatToDTO(Chat chat) {
        return new ChatListDTO(
                chat.getId(),
                chat.getName(),
                chat.getIcon(),
                chat.getMessages().stream().map(this::fromMessageToDTO).toList()
        );
    }

    public ChatMessageDTO fromMessageToDTO(ChatMessage msg) {
        return new ChatMessageDTO(
                msg.getId(),
                msg.getChat().getId(),
                fromUserToDTO(msg.getSender()),
                msg.getContent(),
                msg.getDatetime(),
                msg.getStatus()
        );
    }

    public ChatMemberDTO fromUserToDTO(UserEntity user) {
        return new ChatMemberDTO(
                user.getId(),
                user.getUserEntityName(),
                user.getAvatar(),
                user.getRole().getName()
        );
    }
}
