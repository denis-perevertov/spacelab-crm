package com.example.spacelab.mapper;

import com.example.spacelab.exception.MappingException;
import com.example.spacelab.model.contact.ContactInfo;
import com.example.spacelab.dto.contact.ContactInfoDTO;
import com.example.spacelab.repository.AdminRepository;
import com.example.spacelab.repository.ContactInfoRepository;
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

        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("Entity: " + info);
            throw new MappingException(e.getMessage());
        }

        return info;
    }
}
