package com.example.spacelab.service;

import com.example.spacelab.dto.chat.ChatGroupSaveDTO;
import com.example.spacelab.dto.chat.ChatsAndContactsDTO;
import com.example.spacelab.dto.chat.ClientChatMessage;
import com.example.spacelab.dto.chat.ServerChatMessage;
import com.example.spacelab.model.chat.Chat;
import com.example.spacelab.model.chat.ChatMessage;
import io.swagger.v3.oas.annotations.Hidden;

import java.util.List;

@Hidden
public interface ChatService {

    List<Chat> getChats();
//    List<Chat> getChatsForLoggedInAdmin(String query);

    ChatsAndContactsDTO getChatsForLoggedInAdmin(String query);

    Chat getChat(Long chatId);
    Chat saveChat(Chat chat);
    Chat saveChatGroup(ChatGroupSaveDTO dto);
    void deleteChat(Long chatId);

    List<ChatMessage> getChatMessages(Long chatId);

    ChatMessage getChatMessage(Long msgId);
    ChatMessage saveChatMessage(ChatMessage message);
    ServerChatMessage saveChatMessage(ClientChatMessage clientMsg);
    ServerChatMessage savePrivateMessage(ClientChatMessage clientMsg);
    void deleteChatMessage(Long msgId);
}
