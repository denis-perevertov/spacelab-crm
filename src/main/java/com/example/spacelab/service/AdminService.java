package com.example.spacelab.service;

import com.example.spacelab.model.Admin;
import com.example.spacelab.model.dto.admin.AdminDTO;
import com.example.spacelab.util.FilterForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminService extends EntityFilterService<Admin>{
    List<AdminDTO> getAdmins();
    Page<AdminDTO> getAdmins(Pageable pageable);
    Page<AdminDTO> getAdmins(FilterForm filters, Pageable pageable);
    AdminDTO getAdminById(Long id);
    AdminDTO createAdmin(AdminDTO admin);
    AdminDTO updateAdmin(AdminDTO admin);
    void deleteAdminById(Long id);
}
