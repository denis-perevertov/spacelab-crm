package com.example.spacelab.service;

import com.example.spacelab.model.Admin;
import com.example.spacelab.model.dto.AdminDTO;
import com.example.spacelab.util.FilterForm;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminService extends EntityService<Admin>{
    List<AdminDTO> getAdmins();
    List<AdminDTO> getAdmins(Pageable pageable);
    List<AdminDTO> getAdmins(FilterForm filters, Pageable pageable);
    AdminDTO getAdminById(Long id);
    AdminDTO createAdmin(AdminDTO admin);
    AdminDTO updateAdmin(AdminDTO admin);
    void deleteAdminById(Long id);
}
