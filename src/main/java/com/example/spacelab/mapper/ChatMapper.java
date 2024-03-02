package com.example.spacelab.mapper;

import com.example.spacelab.dto.chat.ChatGroupDTO;
import com.example.spacelab.dto.chat.ChatMemberDTO;
import com.example.spacelab.dto.chat.ChatMessageDTO;
import com.example.spacelab.dto.chat.ChatPrivateDTO;
import com.example.spacelab.model.UserEntity;
import com.example.spacelab.model.chat.Chat;
import com.example.spacelab.model.chat.ChatMessage;
import com.example.spacelab.model.chat.ChatType;
import com.example.spacelab.model.chat.PrivateChat;
import org.springframework.stereotype.Component;

@Component
public class ChatMapper {

    public Object fromChatToDTO(Chat chat) {
        if(chat.getType().equals(ChatType.GROUP)) {
            return new ChatGroupDTO(
                    chat.getId(),
                    chat.getName(),
                    chat.getType(),
                    chat.getIcon(),
                    chat.getMessages().stream().map(this::fromMessageToDTO).toList()
            );
        }
        else {
            PrivateChat pc = (PrivateChat) chat;
            return new ChatPrivateDTO(
                    pc.getId(),
                    new ChatPrivateDTO.User(
                            pc.getPerson1().getId(),
                            pc.getPerson1().getUserEntityName(),
                            pc.getPerson1().getRole().getName(),
                            pc.getPerson1().getAvatar()
                    ),
                    new ChatPrivateDTO.User(
                            pc.getPerson2().getId(),
                            pc.getPerson2().getUserEntityName(),
                            pc.getPerson2().getRole().getName(),
                            pc.getPerson2().getAvatar()
                    ),
                    pc.getType(),
                    pc.getMessages().stream().map(this::fromMessageToDTO).toList()
            );
        }
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
