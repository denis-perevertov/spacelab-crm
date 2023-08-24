package com.example.spacelab.service;

import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.util.FilterForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminService extends EntityFilterService<Admin>{
    List<Admin> getAdmins();
    Page<Admin> getAdmins(Pageable pageable);
    Page<Admin> getAdmins(FilterForm filters, Pageable pageable);

    Admin getAdminById(Long id);
    Admin createAdmin(Admin admin);
    Admin updateAdmin(Admin admin);

    void deleteAdminById(Long id);
}
