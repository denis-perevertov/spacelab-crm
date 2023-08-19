package com.example.spacelab.mapper;

import com.example.spacelab.exception.MappingException;
import com.example.spacelab.model.Admin;
import com.example.spacelab.model.dto.AdminDTO;
import com.example.spacelab.model.dto.CourseDTO.CourseListDTO;
import com.example.spacelab.repository.AdminRepository;
import com.example.spacelab.repository.CourseRepository;
import com.example.spacelab.repository.UserRoleRepository;
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
            dto.setFirstName(admin.getFirstName());
            dto.setLastName(admin.getLastName());
            dto.setFull_name(admin.getFirstName() + " " + admin.getLastName());
            dto.setPhone(admin.getPhone());
            dto.setEmail(admin.getEmail());
            dto.setRole(admin.getRole().toString());

            List<CourseListDTO> courses = dto.getCourses();
            admin.getCourses().forEach(course -> courses.add(courseMapper.fromCourseToDTO(course)));
        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("DTO: " + dto);
            throw new MappingException(e.getMessage());

        }

        return dto;
    }


    public Admin fromDTOToAdmin(AdminDTO dto) {
        if(dto.getId() != null) return adminRepository.getReferenceById(dto.getId());
        else {
            Admin admin = new Admin();

            try {
                admin.setFirstName(dto.getFirstName());
                admin.setLastName(dto.getLastName());
                admin.setPhone(dto.getPhone());
                admin.setEmail(dto.getEmail());
                admin.setPassword(dto.getPassword());

                if(dto.getRole() != null) admin.setRole(userRoleRepository.getReferenceByName(dto.getRole()));

            /*
                TODO
                курсы
             */
            } catch (Exception e) {
                log.severe("Mapping error: " + e.getMessage());
                log.warning("Entity: " + admin);
                throw new MappingException(e.getMessage());
            }

            return admin;


        }
    }
}
