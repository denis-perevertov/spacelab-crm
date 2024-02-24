package com.example.spacelab.mapper;

import com.example.spacelab.dto.contact.ContactInfoDTO;
import com.example.spacelab.dto.contact.ContactInfoEditDTO;
import com.example.spacelab.exception.MappingException;
import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.model.contact.ContactInfo;
import com.example.spacelab.repository.AdminRepository;
import com.example.spacelab.repository.ContactInfoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

@Component
@Log
@RequiredArgsConstructor
public class ContactInfoMapper {

    private final AdminRepository adminRepository;
    private final ContactInfoRepository contactInfoRepository;
    private final AdminMapper adminMapper;

    public  ContactInfoDTO fromContactToContactDTO(ContactInfo info) {

        ContactInfoDTO dto = new ContactInfoDTO();

        try {

            dto.setId(info.getId());
            dto.setName(info.getName());
            dto.setAdmin(adminMapper.fromAdminToContactDTO(info.getAdmin()));
            dto.setPhone(info.getPhone());
            dto.setEmail(info.getEmail());
            dto.setTelegram(info.getTelegram());

        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("DTO: " + dto);
            throw new MappingException(e.getMessage());
        }

        return dto;
    }

    public  ContactInfo fromContactDTOToContact(ContactInfoDTO dto) {

        ContactInfo info = (dto.getId() != null) ?
                            contactInfoRepository.getReferenceById(dto.getId()) :
                            new ContactInfo();

        try {

            info.setId(dto.getId());
            info.setAdmin(adminRepository.getReferenceById(dto.getAdmin().getId()));
            info.setPhone(dto.getPhone());
            info.setEmail(dto.getEmail());
            info.setTelegram(dto.getTelegram());

        } catch (EntityNotFoundException e) {
            log.severe("Error: " + e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("Entity: " + info);
            throw new MappingException(e.getMessage());
        }

        return info;
    }

    public ContactInfo fromEditDTOToContact(ContactInfoEditDTO dto) {

        ContactInfo info = (dto.getId() != null) ?
                contactInfoRepository.getReferenceById(dto.getId()) :
                new ContactInfo();

        try {

            info.setId(dto.getId());
            info.setName(dto.getName());
            info.setAdmin(adminRepository.getReferenceById(dto.getAdminID()));
            info.setPhone(dto.getPhone());
            info.setEmail(dto.getEmail());
            info.setTelegram(dto.getTelegram());

        } catch (EntityNotFoundException e) {
            log.severe("Error: " + e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("Entity: " + info);
            throw new MappingException(e.getMessage());
        }

        return info;

    }

    public ContactInfoEditDTO fromContactToEditDTO(ContactInfo contactInfo) {

        try {
            ContactInfoEditDTO dto = new ContactInfoEditDTO();

            dto.setId(contactInfo.getId());
            dto.setName(contactInfo.getName());
            dto.setAdminID(contactInfo.getAdmin().getId());
            dto.setAdminName(contactInfo.getAdmin().getFirstName() + " " + contactInfo.getAdmin().getLastName());
            dto.setAdminAvatar(contactInfo.getAdmin().getAvatar());
            dto.setRoleName(contactInfo.getAdmin().getRole().getName());
            dto.setEmail(contactInfo.getEmail());
            dto.setPhone(contactInfo.getPhone());
            dto.setTelegram(contactInfo.getTelegram());

            return dto;
        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            throw new MappingException(e.getMessage());
        }
    }
}
