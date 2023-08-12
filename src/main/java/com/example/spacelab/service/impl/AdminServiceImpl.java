package com.example.spacelab.service.impl;

import com.example.spacelab.mapper.AdminDTOMapper;
import com.example.spacelab.model.Admin;
import com.example.spacelab.model.dto.AdminDTO;
import com.example.spacelab.repository.AdminRepository;
import com.example.spacelab.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final AdminDTOMapper adminMapper;

    @Override
    public List<Admin> getAdmins() {
        return null;
    }

    @Override
    public List<AdminDTO> getAdminsByPage(Pageable pageable) {
        return null;
    }

    @Override
    public Admin getAdminById(Long id) {
        return null;
    }

    @Override
    public AdminDTO getAdminDTOById(Long id) {
        return null;
    }

    @Override
    public Admin createAdmin(Admin admin) {
        return null;
    }

    @Override
    public Admin createAdmin(AdminDTO admin) {
        return null;
    }

    @Override
    public Admin updateAdmin(Admin admin) {
        return null;
    }

    @Override
    public void deleteAdminById(Long id) {

    }
}
