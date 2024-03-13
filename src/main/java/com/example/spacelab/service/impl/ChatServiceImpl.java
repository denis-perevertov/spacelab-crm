package com.example.spacelab.service.impl;

import com.example.spacelab.dto.chat.*;
import com.example.spacelab.mapper.ChatMapper;
import com.example.spacelab.model.UserEntity;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.chat.*;
import com.example.spacelab.model.student.Student;
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

import java.util.*;

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

//    @Override
//    public List<Chat> getChatsForLoggedInAdmin(String query) {
//        Admin admin = adminRepository.findById(authUtil.getLoggedInAdmin().getId()).orElseThrow();
//        List<Chat> chats = (query == null || query.isEmpty())
//                ? chatRepository.findChatsForUser(admin.getId())
//                : chatRepository.findChatsForUser(admin.getId(), query);
//
//        // check all admins and students to create new chats ?
//        return chats;
//    }

    @Override
    public ChatsAndContactsDTO getChatsForLoggedInAdmin(String query) {
        Admin loggedInAdmin = adminRepository.findById(authUtil.getLoggedInAdmin().getId()).orElseThrow();
        List<Chat> chats = (query == null || query.isEmpty())
                ? chatRepository.findGroupChatsForUser(loggedInAdmin.getId())
                : chatRepository.findGroupChatsForUser(loggedInAdmin.getId(), query);
        List<Chat> privateChats = chatRepository.findPrivateChatsForUser(loggedInAdmin.getId());
        chats.addAll(privateChats);
        List<Admin> admins = adminRepository.findAll();
        List<Student> students = studentRepository.findAll();
        List<Object> contacts = new ArrayList<>();

        log.info("chats: {}", chats);

        admins.forEach(admin -> {
            // check current admin's private chats , if he does not have a private chat with someone - throw him in as a contact
            for (Chat chat : privateChats) {
                PrivateChat privateChat = (PrivateChat) chat;
                if (
                        privateChat.getPerson1().getId().equals(admin.getId())
                        || privateChat.getPerson2().getId().equals(admin.getId())
                ) {
                    return;
                }
            }
            if(query == null || query.isEmpty() || admin.getFullName().toLowerCase().contains(query.toLowerCase())) {
                // additional check to not send your own contact
                // todo check later
                if(!admin.getId().equals(loggedInAdmin.getId())) {
                    contacts.add(new ChatContactDTO(
                            admin.getId(),
                            admin.getFullName(),
                            admin.getRole().getName(),
                            admin.getAvatar()
                    ));
                }
            }
        });

        students.forEach(student -> {
            // check current admin's private chats , if he does not have a private chat with someone - throw him in as a contact
            for (Chat chat : privateChats) {
                PrivateChat privateChat = (PrivateChat) chat;
                if (
                        privateChat.getPerson1().getId().equals(student.getId())
                        || privateChat.getPerson2().getId().equals(student.getId())
                ) {
                    return;
                }
            }
            if(query == null || query.isEmpty() || student.getFullName().toLowerCase().contains(query.toLowerCase())) {
                contacts.add(new ChatContactDTO(
                        student.getId(),
                        student.getFullName(),
                        student.getRole().getName(),
                        student.getAvatar()
                ));
            }
        });

        return new ChatsAndContactsDTO(
                chats.stream().map(mapper::fromChatToDTO).toList(),
                contacts
        );
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
    public Chat saveChatGroup(ChatGroupSaveDTO dto) {
        GroupChat chat = (dto.id() != null) ? (GroupChat) chatRepository.getReferenceById(dto.id()) : new GroupChat();
        chat.setName(dto.name());

        if(dto.admins() != null && dto.admins().length > 0) {
            Arrays.stream(dto.admins()).forEach(id -> adminRepository.findById(id).ifPresent(foundAdmin -> {
                chat.getMembers().add(foundAdmin);
                foundAdmin.getChats().add(chat);
            }));
        }

        if(dto.students() != null && dto.students().length > 0) {
            Arrays.stream(dto.students()).forEach(id -> studentRepository.findById(id).ifPresent(foundStudent -> {
                chat.getMembers().add(foundStudent);
                foundStudent.getChats().add(chat);
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
        if(clientMsg.recipient() != null) {
            msg.setRecipient(
                    clientMsg.recipient().role().equalsIgnoreCase("student")
                    ? studentRepository.getReferenceById(clientMsg.recipient().id())
                    : adminRepository.getReferenceById(clientMsg.recipient().id())
            );
        }
        msg = messageRepository.save(msg);
        if(!chat.getMessages().contains(msg)) {
            chat.getMessages().add(msg);
        }
        chatRepository.save(chat);
        return new ServerChatMessage(msg);
    }

    @Override
    @Transactional
    public ServerChatMessage savePrivateMessage(ClientChatMessage clientMsg) {
        Optional<Chat> opt = chatRepository.findById(Optional.ofNullable(clientMsg.chatId()).orElse(-1L));
        Chat chat;
        UserEntity sender = clientMsg.sender().role().equalsIgnoreCase("student")
                ? studentRepository.getReferenceById(clientMsg.sender().id())
                : adminRepository.getReferenceById(clientMsg.sender().id());
        UserEntity receiver = clientMsg.recipient().role().equalsIgnoreCase("student")
                ? studentRepository.getReferenceById(clientMsg.recipient().id())
                : adminRepository.getReferenceById(clientMsg.recipient().id());
        if(opt.isEmpty()) {
            // create chat
            PrivateChat privateChat = new PrivateChat()
                    .setPerson1(sender)
                    .setPerson2(receiver);
            chat = chatRepository.save(privateChat);
        }
        else chat = opt.get();
        ChatMessage msg = (clientMsg.msgId() != null ? messageRepository.findById(clientMsg.msgId()).orElseThrow() : new ChatMessage())
                .setId(clientMsg.msgId())
                .setContent(clientMsg.content())
                .setDatetime(clientMsg.datetime())
                .setChat(chat)
                .setStatus(Optional.ofNullable(clientMsg.status()).orElse(ChatMessageStatus.CREATED))
                .setSender(sender)
                .setRecipient(receiver);

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
