package com.example.spacelab.service.impl;

import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.mapper.AdminMapper;
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
    private final AdminMapper adminMapper;

    @Override
    public List<AdminDTO> getAdmins() {
        log.info("Getting admins without filtering or pages");
        return adminRepository.findAll()
                .stream()
                .map(adminMapper::fromAdminToDTO)
                .toList();
    }

    @Override
    public List<AdminDTO> getAdmins(Pageable pageable) {
        log.info("Getting admins with page " + pageable.getPageNumber() + "/ size " + pageable
                .getPageSize());
        return adminRepository.findAll(pageable)
                .stream()
                .map(adminMapper::fromAdminToDTO)
                .toList();
    }

    @Override
    public List<AdminDTO> getAdmins(FilterForm filters, Pageable pageable) {
        log.info("Getting admins with page " + pageable.getPageNumber() + "/ size " + pageable
                .getPageSize() + " and filters: " + filters);
        Specification<Admin> spec = buildSpecificationFromFilters(filters);
        return adminRepository.findAll(spec,pageable)
                .stream()
                .map(adminMapper::fromAdminToDTO)
                .toList();
    }

    @Override
    public AdminDTO getAdminById(Long id) {
        log.info("Getting admin with ID: " + id);
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Admin not found!"));
        return adminMapper.fromAdminToDTO(admin);
    }

    @Override
    public AdminDTO createAdmin(AdminDTO dto) {
        log.info("Creating new admin");
        try {
            Admin admin = adminMapper.fromDTOToAdmin(dto);
            log.info(admin.toString());
            admin = adminRepository.save(admin);
            log.info("Saved admin: " + admin);
            return adminMapper.fromAdminToDTO(admin);
        } catch (ResourceNotFoundException e) {
            log.severe(e.getMessage());
            throw new ResourceNotFoundException("Admin with ID: " + dto.getId() + " not found");
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException("Unknown error during saving");
        }
    }

    @Override
    public AdminDTO updateAdmin(AdminDTO dto) {
        log.info("Updating admin");
        try {
            Admin admin = adminMapper.fromDTOToAdmin(dto);
            log.info(admin.toString());
            admin = adminRepository.save(admin);
            log.info("Saved admin: " + admin);
            return adminMapper.fromAdminToDTO(admin);
        } catch (ResourceNotFoundException e) {
            log.severe(e.getMessage());
            throw new ResourceNotFoundException("Admin with ID: " + dto.getId() + " not found");
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw new RuntimeException("Unknown error during saving");
        }
    }

    @Override
    public void deleteAdminById(Long id) {
        log.info("Deleting admin with ID: " + id);
        adminRepository.deleteById(id);
    }

    @Override
    public Specification<Admin> buildSpecificationFromFilters(FilterForm filters) {

        log.info("Building specification from filters: " + filters);

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
