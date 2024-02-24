package com.example.spacelab.validator;

import com.example.spacelab.dto.admin.AdminEditDTO;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.repository.AdminRepository;
import com.example.spacelab.repository.CourseRepository;
import com.example.spacelab.repository.UserRoleRepository;
import com.example.spacelab.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
@Log
public class AdminValidator implements Validator {

//    private final static String PHONE_PATTERN = "^([+]?[\\s0-9]+)?(\\d{3}|[(]?[0-9]+[)])?([-]?[\\s]?[0-9])+$";
    private final static String PHONE_PATTERN = "^(38)?0(99|50|66|97|96|98)\\d{7}$";
    private final static String EMAIL_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    private final AdminRepository adminRepository;
    private final UserRoleRepository roleRepository;
    private final CourseRepository courseRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Admin.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors e) {
        AdminEditDTO dto = (AdminEditDTO) target;

        if(dto.getFirstName() == null || dto.getFirstName().isEmpty())
            e.rejectValue("firstName", "firstName.empty", "validation.field.empty");
        else if(dto.getFirstName().length() > 50)
            e.rejectValue("firstName", "firstName.length", "validation.field.length.max");

        if(dto.getLastName() == null || dto.getLastName().isEmpty())
            e.rejectValue("lastName", "lastName.empty", "validation.field.empty");
        else if(dto.getLastName().length() > 50)
            e.rejectValue("lastName", "lastName.length", "validation.field.length.max");

        if(dto.getPhone() == null || dto.getPhone().isEmpty())
            e.rejectValue("phone", "phone.empty", "validation.field.empty");
        else if(!dto.getPhone().matches(PHONE_PATTERN))
            e.rejectValue("phone", "phone.no-match", "validation.field.format.allowed");

        if(dto.getEmail() == null || dto.getEmail().isEmpty())
            e.rejectValue("email", "email.empty", "validation.field.empty");
        else if(!dto.getEmail().matches(EMAIL_PATTERN))
            e.rejectValue("email", "email.no-match", "validation.field.format.allowed");
        else if(adminRepository.existsByEmail(dto.getEmail())) {
            if(dto.getId() == null || dto.getId() == 0)
                e.rejectValue("email", "email.exists", "validation.admin.email.exists");
            else if(!dto.getId().equals(adminRepository.findByEmail(dto.getEmail()).orElseThrow().getId()))
                e.rejectValue("email", "email.exists", "validation.admin.email.exists");
        }

        if(dto.getId() == null) {
            if(dto.getPassword() == null || dto.getPassword().isEmpty())
                e.rejectValue("password", "password.empty", "validation.field.empty");
            else if(dto.getPassword().length() < 8 || dto.getPassword().length() > 50)
                e.rejectValue("password", "password.length", "validation.field.length");
            else if(dto.getConfirmPassword() == null || dto.getConfirmPassword().isEmpty())
                e.rejectValue("confirmPassword", "password.confirm", "validation.admin.password.confirm");
            else if(!dto.getConfirmPassword().equals(dto.getPassword())) {
                e.rejectValue("password", "password.no-match", "validation.admin.password.no-match");
                e.rejectValue("confirmPassword", "password.no-match", "validation.admin.password.no-match");
            }
        }
        else if(dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            if(dto.getPassword().length() < 8 || dto.getPassword().length() > 50)
                e.rejectValue("password", "password.length", "validation.field.length");
            else if(dto.getConfirmPassword() == null || dto.getConfirmPassword().isEmpty())
                e.rejectValue("confirmPassword", "password.confirm", "validation.admin.password.confirm");
            else if(!dto.getConfirmPassword().equals(dto.getPassword())) {
                e.rejectValue("password", "password.no-match", "validation.admin.password.no-match");
                e.rejectValue("confirmPassword", "password.no-match", "validation.admin.password.no-match");
            }
        }

        if(dto.getRoleID() == null || dto.getRoleID() == 0)
            e.rejectValue("roleID", "roleID.empty", "validation.field.select");
        else if(!roleRepository.existsById(dto.getRoleID()))
            e.rejectValue("roleID", "roleID.no-match", "validation.role.id.incorrect");

        MultipartFile avatar = dto.getAvatarToSave();
        if(avatar != null && !avatar.isEmpty()) {
            if(avatar.getSize() > ValidationUtils.MAX_IMAGE_SIZE) {
                e.rejectValue("avatarToSave", "avatarToSave.max-size", "validation.file.max-size");
            }
            else {
                String filename = avatar.getOriginalFilename();
                assert filename != null;
                String extension = filename.substring(filename.lastIndexOf(".")+1);
                if(!ValidationUtils.ALLOWED_IMAGE_FORMATS.contains(extension)) {
                    e.rejectValue("avatarToSave", "avatarToSave.extension", "validation.file.extension.allowed");
                }
            }
        }

    }
}
