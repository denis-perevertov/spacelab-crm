package com.example.spacelab.service;

import com.example.spacelab.dto.admin.AdminEditDTO;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.util.FilterForm;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Hidden
public interface AdminService extends EntityFilterService<Admin>, UserDetailsService {
    List<Admin> getAdmins();
    List<Admin> getAdmins(FilterForm filters);
    Page<Admin> getAdmins(Pageable pageable);
    Page<Admin> getAdmins(FilterForm filters, Pageable pageable);

    Admin getAdminById(Long id);
    Admin getAdminByEmail(String email);
    Admin createAdmin(Admin admin);
    Admin updateAdmin(Admin admin);
    Admin createAdmin(AdminEditDTO dto) throws IOException;
    Admin updateAdmin(AdminEditDTO dto) throws IOException;

    boolean canDeleteAdmin(Long id);
    void deleteAdminById(Long id);

    void uploadAvatarForAdmin(Long id, MultipartFile avatar) throws IOException;
    void deleteAvatarForAdmin(Long id);
}
