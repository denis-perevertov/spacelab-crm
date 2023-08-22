package com.example.spacelab.validator;

import com.example.spacelab.model.Admin;
import com.example.spacelab.model.dto.admin.AdminEditDTO;
import com.example.spacelab.repository.AdminRepository;
import com.example.spacelab.repository.CourseRepository;
import com.example.spacelab.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
@Log
public class AdminValidator implements Validator {

    private final static String PHONE_PATTERN = "^([+]?[\\s0-9]+)?(\\d{3}|[(]?[0-9]+[)])?([-]?[\\s]?[0-9])+$";
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
            e.rejectValue("firstName", "firstName.empty", "Enter first name!");
        else if(dto.getFirstName().length() < 2 || dto.getFirstName().length() > 50)
            e.rejectValue("firstName", "firstName.length", "Name length: 2-50");

        if(dto.getLastName() == null || dto.getLastName().isEmpty())
            e.rejectValue("lastName", "lastName.empty", "Enter last name!");
        else if(dto.getLastName().length() < 2 || dto.getLastName().length() > 50)
            e.rejectValue("lastName", "lastName.length", "Name length: 2-50");

        if(dto.getPhone() == null || dto.getPhone().isEmpty())
            e.rejectValue("phone", "phone.empty", "Enter phone!");
        else if(!dto.getPhone().matches(PHONE_PATTERN))
            e.rejectValue("phone", "phone.no-match", "Incorrect phone format!");

        if(dto.getEmail() == null || dto.getEmail().isEmpty())
            e.rejectValue("email", "email.empty", "Enter email!");
        else if(!dto.getEmail().matches(EMAIL_PATTERN))
            e.rejectValue("email", "email.no-match", "Incorrect email format!");
        else if(adminRepository.existsByEmail(dto.getEmail()))
            e.rejectValue("email", "email.exists", "Admin with this email already exists!");

        if(dto.getId() == null && (dto.getPassword() == null || dto.getPassword().isEmpty()))
            e.rejectValue("password", "password.empty", "Enter password!");
        else if(dto.getPassword().length() < 8 || dto.getPassword().length() > 50)
            e.rejectValue("password", "password.length", "Password length: 8-50");
        else if(dto.getConfirmPassword() == null || dto.getConfirmPassword().isEmpty())
            e.rejectValue("confirmPassword", "password.confirm", "Confirm password!");
        else if(!dto.getConfirmPassword().equals(dto.getPassword())) {
            e.rejectValue("password", "password.no-match", "Passwords don't match!");
            e.rejectValue("confirmPassword", "password.no-match", "Passwords don't match!");
        }

//        if(dto.getRoleID() == null || dto.getRoleID() == 0)
//            e.rejectValue("roleID", "roleID.empty", "No role selected");
//        else if(!roleRepository.existsById(dto.getRoleID()))
//            e.rejectValue("roleID", "roleID.no-match", "No role with this ID exists");
//
//        if(dto.getCourseID() == null || dto.getCourseID() == 0)
//            e.rejectValue("courseID", "courseID.empty", "No course selected");
//        else if(!courseRepository.existsById(dto.getCourseID()))
//            e.rejectValue("courseID", "courseID.no-match", "No course with this ID exists");




    }
}
