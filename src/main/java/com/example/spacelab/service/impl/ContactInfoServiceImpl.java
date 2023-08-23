package com.example.spacelab.service.impl;

import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.mapper.ContactInfoMapper;
import com.example.spacelab.model.ContactInfo;
import com.example.spacelab.model.dto.contact.ContactInfoDTO;
import com.example.spacelab.repository.ContactInfoRepository;
import com.example.spacelab.service.ContactInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
@RequiredArgsConstructor
public class ContactInfoServiceImpl implements ContactInfoService {

    private final ContactInfoRepository contactRepository;
    private final ContactInfoMapper contactInfoMapper;


    @Override
    public List<ContactInfo> getContacts() {
        log.info("Getting all contacts...");
        return contactRepository.findAll();
    }

    @Override
    public Page<ContactInfo> getContacts(Pageable pageable) {
        log.info("Getting contacts (page "+pageable.getPageNumber()+")");
        return contactRepository.findAll(pageable);
    }

    @Override
    public ContactInfo getContact(Long id) {
        log.info("Getting contact with ID: " + id);
        ContactInfo info = contactRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact not found"));
        return info;
    }

    @Override
    public ContactInfo saveContact(ContactInfo info) {
        info = contactRepository.save(info);
        log.info("Created contact: " + info);
        return info;
    }

    @Override
    public ContactInfo editContact(ContactInfo info) {
        info = contactRepository.save(info);
        log.info("Edited contact: " + info);
        return info;
    }

    @Override
    public void deleteContact(Long id) {
        ContactInfo info = contactRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact not found"));
        log.info("Deleting contact with ID: " + id);
        contactRepository.delete(info);
    }
}
