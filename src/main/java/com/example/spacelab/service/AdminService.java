package com.example.spacelab.service;

import com.example.spacelab.model.Admin;
import com.example.spacelab.model.dto.AdminDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminService {
    List<Admin> getAdmins();
    List<AdminDTO> getAdminsByPage(Pageable pageable);
    Admin getAdminById(Long id);
    AdminDTO getAdminDTOById(Long id);
    Admin createAdmin(Admin admin);
    Admin createAdmin(AdminDTO admin);
    Admin updateAdmin(Admin admin);
    void deleteAdminById(Long id);
}
