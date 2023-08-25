package com.example.spacelab.mapper;

import com.example.spacelab.exception.MappingException;
import com.example.spacelab.exception.ResourceNotFoundException;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.dto.admin.AdminDTO;
import com.example.spacelab.dto.CourseDTO;
import com.example.spacelab.dto.admin.AdminContactDTO;
import com.example.spacelab.dto.admin.AdminEditDTO;
import com.example.spacelab.repository.AdminRepository;
import com.example.spacelab.repository.CourseRepository;
import com.example.spacelab.repository.UserRoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log
@RequiredArgsConstructor
public class AdminMapper {

    private final AdminRepository adminRepository;
    private final UserRoleRepository userRoleRepository;
    private final CourseRepository courseRepository;

    private final CourseMapper courseMapper;

    public AdminDTO fromAdminToDTO(Admin admin) {
        AdminDTO dto = new AdminDTO();

        try {
            dto.setId(admin.getId());
            dto.setFirstName(admin.getFirstName());
            dto.setLastName(admin.getLastName());
            dto.setFullName(admin.getFirstName() + " " + admin.getLastName());
            dto.setPhone(admin.getPhone());
            dto.setEmail(admin.getEmail());

            if(admin.getRole() != null)
                dto.setRole(admin.getRole().getName());

            List<CourseDTO> courses = dto.getCourses();
            admin.getCourses().forEach(course -> courses.add(courseMapper.fromCourseToDTO(course)));
        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("DTO: " + dto);
            throw new MappingException(e.getMessage());

        }

        return dto;
    }

    public AdminContactDTO fromAdminToContactDTO(Admin admin) {
        AdminContactDTO dto = new AdminContactDTO();

        try {

            dto.setId(admin.getId());
            dto.setAvatar(admin.getAvatar());
            dto.setFullName(admin.getFirstName() + " " + admin.getLastName());
            dto.setRoleName(admin.getRole().getName());

        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("DTO: " + dto);
            throw new MappingException(e.getMessage());
        }

        return dto;
    }

    public AdminEditDTO fromAdminToEditDTO(Admin admin) {
        AdminEditDTO dto = new AdminEditDTO();

        try {
            dto.setFirstName(admin.getFirstName());
            dto.setLastName(admin.getLastName());
            dto.setPhone(admin.getPhone());
            dto.setEmail(admin.getEmail());
            dto.setRoleID(admin.getRole().getId());

            /*
                TODO
                курсы
             */

        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("DTO: " + dto);
            throw new MappingException(e.getMessage());
        }

        return dto;
    }

    public Admin fromEditDTOToAdmin(AdminEditDTO dto) {
        Admin admin = (dto.getId() != null && dto.getId() != 0) ?
                adminRepository.getReferenceById(dto.getId()) :
                new Admin();

        try {
            admin.setFirstName(dto.getFirstName());
            admin.setLastName(dto.getLastName());
            admin.setPhone(dto.getPhone());
            admin.setEmail(dto.getEmail());
            if((admin.getPassword() == null || admin.getPassword().isEmpty()) ||
                (dto.getPassword() != null && !dto.getPassword().isEmpty()))
                admin.setPassword(dto.getPassword());

            /*
                TODO
                курсы
             */

            if(dto.getRoleID() != null) admin.setRole(userRoleRepository.getReferenceById(dto.getRoleID()));

        } catch (EntityNotFoundException e) {
            log.severe("Mapping error: " + e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("Entity: " + admin);
            throw new MappingException(e.getMessage());
        }

        return admin;
    }

/*
    public Admin fromDTOToAdmin(AdminDTO dto) {
        if(dto.getId() != null) return adminRepository.getReferenceById(dto.getId());
        else {
            Admin admin = new Admin();

            try {
                admin.setFirstName(dto.getFirstName());
                admin.setLastName(dto.getLastName());
                admin.setPhone(dto.getPhone());
                admin.setEmail(dto.getEmail());

                if(dto.getRole() != null) admin.setRole(userRoleRepository.getReferenceByName(dto.getRole()));

            *//*
                TODO
                курсы
             *//*

            } catch (Exception e) {
                log.severe("Mapping error: " + e.getMessage());
                log.warning("Entity: " + admin);
                throw new MappingException(e.getMessage());
            }

            return admin;


        }
    }*/
}
