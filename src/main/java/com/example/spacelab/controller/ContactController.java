package com.example.spacelab.controller;

import com.example.spacelab.mapper.ContactInfoMapper;
import com.example.spacelab.model.contact.ContactInfo;
import com.example.spacelab.dto.contact.ContactInfoDTO;
import com.example.spacelab.dto.contact.ContactInfoEditDTO;
import com.example.spacelab.service.ContactInfoService;
import com.example.spacelab.validator.ContactValidator;
import com.example.spacelab.validator.ValidationErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@Log
@RequiredArgsConstructor
@RequestMapping("/contacts")
public class ContactController {

    private final ContactInfoService contactService;
    private final ContactInfoMapper contactMapper;
    private final ContactValidator contactValidator;

    // Получение всех контактов
    @GetMapping
    public ResponseEntity<Page<ContactInfoDTO>> getContacts(@RequestParam(required = false) Integer page,
                                                            @RequestParam(required = false) Integer size) {
        Page<ContactInfoDTO> contactList;
        if(page == null || size == null) contactList = new PageImpl<>(contactService.getContacts().stream().map(contactMapper::fromContactToContactDTO).toList());
        else contactList = new PageImpl<>(contactService.getContacts(PageRequest.of(page, size)).stream().map(contactMapper::fromContactToContactDTO).toList());

        return new ResponseEntity<>(contactList, HttpStatus.OK);
    }

    // Получение одного контакта
    @GetMapping("/{id}")
    public ResponseEntity<ContactInfoDTO> getContact(@PathVariable Long id) {
        ContactInfoDTO info = contactMapper.fromContactToContactDTO(contactService.getContact(id));
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    // Добавление нового контакта
    @PostMapping
    public ResponseEntity<?> createNewContact(@RequestBody ContactInfoEditDTO contactInfoDTO,
                                                           BindingResult bindingResult) {

        contactValidator.validate(contactInfoDTO, bindingResult);

        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return new ResponseEntity<>(new ValidationErrorMessage(HttpStatus.BAD_REQUEST.value(), errors), HttpStatus.BAD_REQUEST);
        }

        ContactInfo info = contactService.saveContact(contactMapper.fromEditDTOToContact(contactInfoDTO));
        return new ResponseEntity<>(contactMapper.fromContactToContactDTO(info), HttpStatus.OK);
    }

    // Редактирование контакта
    @PutMapping("/{id}")
    public ResponseEntity<?> editContact(@PathVariable Long id,
                                          @RequestBody ContactInfoEditDTO contactInfoDTO,
                                          BindingResult bindingResult) {
        contactInfoDTO.setId(id);

        contactValidator.validate(contactInfoDTO, bindingResult);

        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return new ResponseEntity<>(new ValidationErrorMessage(HttpStatus.BAD_REQUEST.value(), errors), HttpStatus.BAD_REQUEST);
        }

        ContactInfo info = contactService.editContact(contactMapper.fromEditDTOToContact(contactInfoDTO));
        return new ResponseEntity<>(contactMapper.fromContactToContactDTO(info), HttpStatus.OK);
    }

    // Удаление контакта
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return new ResponseEntity<>("Contact with ID: " + id + " deleted", HttpStatus.OK);
    }
}
