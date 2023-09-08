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


        if(dto.getMentorID() == null || dto.getMentorID() == 0)
            e.rejectValue("mentorID", "mentorID.empty", "Select mentor!");
        else if(!adminRepository.existsById(dto.getMentorID()))
            e.rejectValue("mentorID", "mentorID.no-match", "Mentor with this ID doesn't exist!");


        if(dto.getManagerID() == null || dto.getManagerID() == 0)
            e.rejectValue("managerID", "managerID.empty", "Select manager!");
        else if(!adminRepository.existsById(dto.getManagerID()))
            e.rejectValue("managerID", "managerID.no-match", "Manager with this ID doesn't exist!");


        if(dto.getBeginDate() == null)
            e.rejectValue("beginDate", "beginDate.empty", "Enter beginning date!");
        else if(dto.getBeginDate().isBefore(java.time.LocalDate.now()))
            e.rejectValue("beginDate", "beginDate.no-match", "Beginning date can't be in the past!");
        else if(dto.getBeginDate().isEqual(java.time.LocalDate.now()))
            e.rejectValue("beginDate", "beginDate.no-match", "Beginning date must be in future!");
        else if(dto.getBeginDate().isAfter(java.time.LocalDate.now().plusYears(1)))
            e.rejectValue("beginDate", "beginDate.no-match", "Beginning date can't be later than one year!");


    }
}
