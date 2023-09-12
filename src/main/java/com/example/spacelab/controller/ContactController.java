package com.example.spacelab.controller;

import com.example.spacelab.dto.admin.AdminDTO;
import com.example.spacelab.exception.ErrorMessage;
import com.example.spacelab.exception.ObjectValidationException;
import com.example.spacelab.mapper.ContactInfoMapper;
import com.example.spacelab.model.contact.ContactInfo;
import com.example.spacelab.dto.contact.ContactInfoDTO;
import com.example.spacelab.dto.contact.ContactInfoEditDTO;
import com.example.spacelab.service.ContactInfoService;
import com.example.spacelab.validator.ContactValidator;
import com.example.spacelab.exception.ValidationErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name="Contact", description = "Contact info controller")
@RestController
@Log
@RequiredArgsConstructor
@RequestMapping("/api/contacts")
public class ContactController {

    private final ContactInfoService contactService;
    private final ContactInfoMapper contactMapper;
    private final ContactValidator contactValidator;


    // Получение всех контактов
    @Operation(description = "Get list of contacts paginated by 'page/size' params (default values are 0/10)", summary = "Get Contacts", tags = {"Contact"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('settings.read.NO_ACCESS')")
    @GetMapping
    public ResponseEntity<Page<ContactInfoDTO>> getContacts(@RequestParam(required = false, defaultValue = "0") Integer page,
                                                            @RequestParam(required = false , defaultValue = "10") Integer size) {
        Page<ContactInfoDTO> contactList;
        if(page == null || size == null) contactList = new PageImpl<>(contactService.getContacts().stream().map(contactMapper::fromContactToContactDTO).toList());
        else contactList = new PageImpl<>(contactService.getContacts(PageRequest.of(page, size)).stream().map(contactMapper::fromContactToContactDTO).toList());

        return new ResponseEntity<>(contactList, HttpStatus.OK);
    }

    // Получение одного контакта
    @Operation(description = "Get contact info DTO by its ID", summary = "Get Contact By ID", tags = {"Contact"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Contact not found in DB", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('settings.read.NO_ACCESS')")
    @GetMapping("/{id}")
    public ResponseEntity<ContactInfoDTO> getContact(@PathVariable @Parameter(example = "1") Long id) {
        ContactInfoDTO info = contactMapper.fromContactToContactDTO(contactService.getContact(id));
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    // Добавление нового контакта
    @Operation(description = "Create new contact card (one for each admin); ID field does not matter in write/edit operations", summary = "Create New Contact", tags = {"Contact"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created"),
            @ApiResponse(responseCode = "400", description = "Bad Request / Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('settings.write.NO_ACCESS')")
    @PostMapping
    public ResponseEntity<ContactInfoDTO> createNewContact(@RequestBody ContactInfoEditDTO contactInfoDTO,
                                                           BindingResult bindingResult) {

        contactValidator.validate(contactInfoDTO, bindingResult);

        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ObjectValidationException(errors);
        }
        ContactInfo info = contactService.saveContact(contactMapper.fromEditDTOToContact(contactInfoDTO));
        return new ResponseEntity<>(contactMapper.fromContactToContactDTO(info), HttpStatus.OK);
    }

    // Редактирование контакта
    @Operation(description = "Update existing contact in the application; ID field does not matter in write/edit operations", summary = "Update Contact", tags = {"Contact"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "400", description = "Bad Request / Validation Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access Denied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('settings.edit.NO_ACCESS')")
    @PutMapping("/{id}")
    public ResponseEntity<ContactInfoDTO> editContact(@PathVariable @Parameter(example = "1") Long id,
                                          @RequestBody ContactInfoEditDTO contactInfoDTO,
                                          BindingResult bindingResult) {
        contactInfoDTO.setId(id);

        contactValidator.validate(contactInfoDTO, bindingResult);

        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            throw new ObjectValidationException(errors);
        }
        ContactInfo info = contactService.editContact(contactMapper.fromEditDTOToContact(contactInfoDTO));
        return new ResponseEntity<>(contactMapper.fromContactToContactDTO(info), HttpStatus.OK);
    }

    // Удаление контакта
    @Operation(description = "Delete contact by his ID", summary = "Delete Contact", tags = {"Contact"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Contact not found in DB", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PreAuthorize("!hasAuthority('settings.delete.NO_ACCESS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteContact(@PathVariable @Parameter(example = "1") Long id) {
        contactService.deleteContact(id);
        return new ResponseEntity<>("Contact with ID: " + id + " deleted", HttpStatus.OK);
    }
}
