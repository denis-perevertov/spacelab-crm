package com.example.spacelab.service;

import com.example.spacelab.model.dto.contact.ContactInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContactInfoService {
    List<ContactInfoDTO> getContacts();
    Page<ContactInfoDTO> getContacts(Pageable pageable);
    ContactInfoDTO getContact(Long id);
    ContactInfoDTO saveContact(ContactInfoDTO dto);
    ContactInfoDTO editContact(ContactInfoDTO dto);
    void deleteContact(Long id);
}
