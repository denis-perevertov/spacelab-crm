package com.example.spacelab.validator;

import com.example.spacelab.model.Student;
import com.example.spacelab.model.dto.student.StudentEditDTO;
import com.example.spacelab.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class StudentValidator implements Validator {

    private final static String PHONE_PATTERN = "^([+]?[\\s0-9]+)?(\\d{3}|[(]?[0-9]+[)])?([-]?[\\s]?[0-9])+$";
    private final static String EMAIL_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private final static String TELEGRAM_PATTERN = "^@[A-Za-z0-9_.]{3,}$";

    private final StudentRepository studentRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Student.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors e) {
        StudentEditDTO dto = (StudentEditDTO) target;

        if(dto.firstName() == null || dto.firstName().isEmpty())
            e.rejectValue("firstName", "firstName.empty", "Enter first name!");
        else if(dto.firstName().length() < 2 || dto.firstName().length() > 50)
            e.rejectValue("firstName", "firstName.length", "Name length: 2-50");

        if(dto.lastName() == null || dto.lastName().isEmpty())
            e.rejectValue("lastName", "lastName.empty", "Enter last name!");
        else if(dto.lastName().length() < 2 || dto.lastName().length() > 50)
            e.rejectValue("lastName", "lastName.length", "Name length: 2-50");

        if(dto.fathersName() == null || dto.fathersName().isEmpty())
            e.rejectValue("fathersName", "fathersName.empty", "Enter fathers name!");
        else if(dto.fathersName().length() < 2 || dto.fathersName().length() > 50)
            e.rejectValue("fathersName", "fathersName.length", "Name length: 2-50");

        if(dto.email() == null || dto.email().isEmpty())
            e.rejectValue("email", "email.empty", "Enter email!");
        else if(!dto.email().matches(EMAIL_PATTERN))
            e.rejectValue("email", "email.no-match", "Incorrect email format!");
        else if(studentRepository.existsByDetailsEmail(dto.email())) {
            if(dto.id() == null)
                e.rejectValue("email", "email.taken", "Student with this email already exists!");
            else if(!studentRepository.findById(dto.id()).get().getDetails().getEmail().equals(dto.email()))
                e.rejectValue("email", "email.taken", "Student with this email already exists!");
        }

        if(dto.phone() == null || dto.phone().isEmpty())
            e.rejectValue("phone", "phone.empty", "Enter phone!");
        else if(!dto.phone().matches(PHONE_PATTERN))
            e.rejectValue("phone", "phone.no-match", "Incorrect phone format!");

        if(dto.telegram() == null || dto.telegram().isEmpty())
            e.rejectValue("telegram", "telegram.empty", "Enter telegram!");
        else if(!dto.telegram().matches(TELEGRAM_PATTERN))
            e.rejectValue("telegram", "telegram.no-match", "Incorrect telegram format!");

    }
}
