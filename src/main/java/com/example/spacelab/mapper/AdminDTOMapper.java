package com.example.spacelab.mapper;

import com.example.spacelab.model.Admin;
import com.example.spacelab.model.dto.AdminDTO;
import com.example.spacelab.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminDTOMapper {

    private final AdminRepository adminRepository;

    public AdminDTO fromAdminToDTO(Admin admin) {
        AdminDTO dto = new AdminDTO();


        return dto;
    }


    public Admin fromDTOToAdmin(AdminDTO dto) {
        if(dto.getId() != null) return adminRepository.getReferenceById(dto.getId());
        else {
            Admin admin = new Admin();


            return admin;
        }
    }
}
