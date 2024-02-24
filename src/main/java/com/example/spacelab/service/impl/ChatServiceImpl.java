package com.example.spacelab.service.impl;

import com.example.spacelab.dto.chat.ChatGroupSaveDTO;
import com.example.spacelab.dto.chat.ClientChatMessage;
import com.example.spacelab.dto.chat.ServerChatMessage;
import com.example.spacelab.mapper.ChatMapper;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.chat.Chat;
import com.example.spacelab.model.chat.ChatMessage;
import com.example.spacelab.model.chat.ChatMessageStatus;
import com.example.spacelab.repository.AdminRepository;
import com.example.spacelab.repository.StudentRepository;
import com.example.spacelab.repository.chat.ChatMessageRepository;
import com.example.spacelab.repository.chat.ChatRepository;
import com.example.spacelab.service.ChatService;
import com.example.spacelab.service.FileService;
import com.example.spacelab.util.AuthUtil;
import com.example.spacelab.util.FilenameUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatMessageRepository messageRepository;
    private final ChatMapper mapper;

    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;

    private final FileService fileService;
    private final AuthUtil authUtil;

    @Override
    public List<Chat> getChats() {
        return null;
    }

    @Override
    public List<Chat> getChatsForLoggedInAdmin() {
        Admin admin = adminRepository.findById(authUtil.getLoggedInAdmin().getId()).orElseThrow();
        return admin.getChats();
    }

    @Override
    public Chat getChat(Long chatId) {
        return null;
    }

    @Override
    public Chat saveChat(Chat chat) {
        return null;
    }

    @Override
    public Chat saveChat(ChatGroupSaveDTO dto) {
        Chat chat = (dto.id() != null) ? chatRepository.getReferenceById(dto.id()) : new Chat();
        chat.setName(dto.name());

        if(dto.admins() != null && dto.admins().length > 0) {
            Arrays.stream(dto.admins()).forEach(id -> adminRepository.findById(id).ifPresent(foundAdmin -> {
                chat.getMembers().add(foundAdmin);
            }));
        }

        if(dto.students() != null && dto.students().length > 0) {
            Arrays.stream(dto.students()).forEach(id -> studentRepository.findById(id).ifPresent(foundStudent -> {
                chat.getMembers().add(foundStudent);
            }));
        }

        if(dto.icon() != null && !dto.icon().isEmpty()) {
            try {
                String filename = FilenameUtils.generateFileName(dto.icon());
                fileService.saveFile(dto.icon(), filename, "chats");
                chat.setIcon(filename);
            } catch (Exception e) {
                log.warn("Could not save chat icon: {}", e.getMessage());
            }
        }
        return chatRepository.save(chat);
    }

    @Override
    public void deleteChat(Long chatId) {

    }

    @Override
    public List<ChatMessage> getChatMessages(Long chatId) {
        return null;
    }

    @Override
    public ChatMessage getChatMessage(Long msgId) {
        return null;
    }

    @Override
    public ChatMessage saveChatMessage(ChatMessage message) {
        return null;
    }

    @Override
    @Transactional
    public ServerChatMessage saveChatMessage(ClientChatMessage clientMsg) {
        Chat chat = chatRepository.findById(clientMsg.chatId()).orElseThrow();
        ChatMessage msg = (clientMsg.msgId() != null ? messageRepository.findById(clientMsg.msgId()).orElseThrow() : new ChatMessage())
                .setId(clientMsg.msgId())
                .setContent(clientMsg.content())
                .setDatetime(clientMsg.datetime())
                .setChat(chat)
                .setStatus(Optional.ofNullable(clientMsg.status()).orElse(ChatMessageStatus.CREATED))
                .setSender(
                        clientMsg.sender().role().equalsIgnoreCase("student")
                        ? studentRepository.getReferenceById(clientMsg.sender().id())
                        : adminRepository.getReferenceById(clientMsg.sender().id())
                );
        msg = messageRepository.save(msg);
        if(!chat.getMessages().contains(msg)) {
            chat.getMessages().add(msg);
        }
        chatRepository.save(chat);
        return new ServerChatMessage(msg);
    }

    @Override
    public void deleteChatMessage(Long msgId) {

    }
}
