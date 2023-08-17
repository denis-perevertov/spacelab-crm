package com.example.spacelab.service.impl;

import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.mapper.ContactInfoMapper;
import com.example.spacelab.model.ContactInfo;
import com.example.spacelab.model.dto.ContactInfoDTO;
import com.example.spacelab.repository.ContactInfoRepository;
import com.example.spacelab.service.ContactInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
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
    public List<ContactInfoDTO> getContacts() {
        return contactRepository.findAll().stream()
                .map(contactInfoMapper::fromContactToContactDTO).toList();
    }

    @Override
    public List<ContactInfoDTO> getContacts(Pageable pageable) {
        return contactRepository.findAll(pageable).stream()
                .map(contactInfoMapper::fromContactToContactDTO).toList();
    }

    @Override
    public ContactInfoDTO getContact(Long id) {
        ContactInfo info = contactRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact not found"));
        return contactInfoMapper.fromContactToContactDTO(info);
    }

    @Override
    public ContactInfoDTO saveContact(ContactInfoDTO dto) {
        ContactInfo info = contactInfoMapper.fromContactDTOToContact(dto);
        info = contactRepository.save(info);
        return contactInfoMapper.fromContactToContactDTO(info);
    }

    @Override
    public ContactInfoDTO editContact(ContactInfoDTO dto) {
        ContactInfo info = contactInfoMapper.fromContactDTOToContact(dto);
        info = contactRepository.save(info);
        return contactInfoMapper.fromContactToContactDTO(info);
    }

    @Override
    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }
}
