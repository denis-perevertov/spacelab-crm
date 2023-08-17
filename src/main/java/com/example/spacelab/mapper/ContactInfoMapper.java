package com.example.spacelab.mapper;

import com.example.spacelab.model.ContactInfo;
import com.example.spacelab.model.dto.ContactInfoDTO;
import com.example.spacelab.repository.AdminRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ContactInfoMapper {

    private final AdminRepository adminRepository;

    public  ContactInfoDTO fromContactToContactDTO(ContactInfo info) {

        return new ContactInfoDTO();
    }

    public  ContactInfo fromContactDTOToContact(ContactInfoDTO dto) {

        return new ContactInfo();
    }
}
