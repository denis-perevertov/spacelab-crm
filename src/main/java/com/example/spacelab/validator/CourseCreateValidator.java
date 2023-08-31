package com.example.spacelab.validator;

import com.example.spacelab.dto.course.CourseSaveCreatedDTO;
import com.example.spacelab.dto.student.StudentEditDTO;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.repository.AdminRepository;
import com.example.spacelab.repository.CourseRepository;
import com.example.spacelab.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class CourseCreateValidator implements Validator {


    private final CourseRepository courseRepository;
    private final AdminRepository adminRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Course.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors e) {
        CourseSaveCreatedDTO dto = (CourseSaveCreatedDTO) target;

        if(dto.getName() == null || dto.getName().isEmpty())
            e.rejectValue("name", "name.empty", "Enter name!");
        else if(dto.getName().length() < 2 || dto.getName().length() > 100)
            e.rejectValue("name", "name.length", "Name length: 2-100");


        if(dto.getMentorId() == null || dto.getMentorId() == 0)
            e.rejectValue("mentorID", "mentorID.empty", "Select mentor!");
        else if(!adminRepository.existsById(dto.getMentorId()))
            e.rejectValue("mentorID", "mentorID.no-match", "Mentor with this ID doesn't exist!");


        if(dto.getManagerId() == null || dto.getManagerId() == 0)
            e.rejectValue("managerID", "managerID.empty", "Select manager!");
        else if(!adminRepository.existsById(dto.getManagerId()))
            e.rejectValue("managerID", "managerID.no-match", "Manager with this ID doesn't exist!");


        if(dto.getBeginningDate() == null)
            e.rejectValue("beginningDate", "beginningDate.empty", "Enter beginning date!");
        else if(dto.getBeginningDate().isBefore(java.time.LocalDate.now()))
            e.rejectValue("beginningDate", "beginningDate.no-match", "Beginning date can't be in the past!");
        else if(dto.getBeginningDate().isEqual(java.time.LocalDate.now()))
            e.rejectValue("beginningDate", "beginningDate.no-match", "Beginning date must be in future!");
        else if(dto.getBeginningDate().isAfter(java.time.LocalDate.now().plusYears(1)))
            e.rejectValue("beginningDate", "beginningDate.no-match", "Beginning date can't be later than one year!");


    }
}
