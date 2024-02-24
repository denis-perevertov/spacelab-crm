package com.example.spacelab.service;

import com.example.spacelab.dto.chat.ChatGroupSaveDTO;
import com.example.spacelab.dto.chat.ClientChatMessage;
import com.example.spacelab.dto.chat.ServerChatMessage;
import com.example.spacelab.model.chat.Chat;
import com.example.spacelab.model.chat.ChatMessage;

import java.util.List;

public interface ChatService {

    List<Chat> getChats();
    List<Chat> getChatsForLoggedInAdmin();

    Chat getChat(Long chatId);
    Chat saveChat(Chat chat);
    Chat saveChat(ChatGroupSaveDTO dto);
    void deleteChat(Long chatId);

    List<ChatMessage> getChatMessages(Long chatId);

    ChatMessage getChatMessage(Long msgId);
    ChatMessage saveChatMessage(ChatMessage message);
    ServerChatMessage saveChatMessage(ClientChatMessage clientMsg);
    void deleteChatMessage(Long msgId);
}
