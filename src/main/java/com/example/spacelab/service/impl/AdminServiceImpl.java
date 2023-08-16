package com.example.spacelab.service.impl;

import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.mapper.AdminDTOMapper;
import com.example.spacelab.model.Admin;
import com.example.spacelab.model.Course;
import com.example.spacelab.model.UserRole;
import com.example.spacelab.model.dto.AdminDTO;
import com.example.spacelab.repository.AdminRepository;
import com.example.spacelab.repository.CourseRepository;
import com.example.spacelab.repository.UserRoleRepository;
import com.example.spacelab.service.AdminService;
import com.example.spacelab.service.specification.AdminSpecifications;
import com.example.spacelab.util.FilterForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final UserRoleRepository userRoleRepository;
    private final CourseRepository courseRepository;
    private final AdminDTOMapper adminMapper;

    @Override
    public List<AdminDTO> getAdmins() {
        return adminRepository.findAll()
                .stream()
                .map(adminMapper::fromAdminToDTO)
                .toList();
    }

    @Override
    public List<AdminDTO> getAdmins(Pageable pageable) {
        return adminRepository.findAll(pageable)
                .stream()
                .map(adminMapper::fromAdminToDTO)
                .toList();
    }

    @Override
    public List<AdminDTO> getAdmins(FilterForm filters, Pageable pageable) {
        Specification<Admin> spec = buildSpecificationFromFilters(filters);
        return adminRepository.findAll(spec,pageable)
                .stream()
                .map(adminMapper::fromAdminToDTO)
                .toList();
    }

    @Override
    public AdminDTO getAdminById(Long id) {
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Admin not found!"));
        return adminMapper.fromAdminToDTO(admin);
    }

    @Override
    public AdminDTO createAdmin(AdminDTO dto) {
        Admin admin = adminMapper.fromDTOToAdmin(dto);
        admin = adminRepository.save(admin);
        return adminMapper.fromAdminToDTO(admin);
    }

    @Override
    public AdminDTO updateAdmin(AdminDTO dto) {
        Admin admin = adminMapper.fromDTOToAdmin(dto);
        admin = adminRepository.save(admin);
        return adminMapper.fromAdminToDTO(admin);
    }

    @Override
    public void deleteAdminById(Long id) {
        adminRepository.deleteById(id);
    }

    @Override
    public Specification<Admin> buildSpecificationFromFilters(FilterForm filters) {

        String name = filters.getName();
        Long roleID = filters.getRole();
        Long courseID = filters.getCourse();
        String dateString = filters.getDate();
        String email = filters.getEmail();
        String phone = filters.getPhone();

        UserRole role = (roleID == null) ? null : userRoleRepository.getReferenceById(roleID);
        Course course = (courseID == null) ? null : courseRepository.getReferenceById(courseID);

        /*
            TODO
            добавить фильтр по дате рождения (если он нужен)
        */

        Specification<Admin> spec = Specification.where(
                AdminSpecifications.hasNameLike(name)
                        .and(AdminSpecifications.hasRole(role))
                        .and(AdminSpecifications.hasCourse(course))
                        .and(AdminSpecifications.hasEmailLike(email))
                        .and(AdminSpecifications.hasPhoneLike(phone))
        );

        return spec;
    }
}
