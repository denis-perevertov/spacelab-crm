package com.example.spacelab.service;

import com.example.spacelab.model.ContactInfo;
import com.example.spacelab.model.dto.ContactInfoDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContactInfoService {
    List<ContactInfoDTO> getContacts();
    List<ContactInfoDTO> getContacts(Pageable pageable);
    ContactInfoDTO getContact(Long id);
    ContactInfoDTO saveContact(ContactInfoDTO dto);
    ContactInfoDTO editContact(ContactInfoDTO dto);
    void deleteContact(Long id);
}
