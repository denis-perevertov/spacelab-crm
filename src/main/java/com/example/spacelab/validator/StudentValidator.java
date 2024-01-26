package com.example.spacelab.validator;

import com.example.spacelab.dto.student.StudentTaskUnlockRequest;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.dto.student.StudentEditDTO;
import com.example.spacelab.model.task.Task;
import com.example.spacelab.repository.StudentRepository;
import com.example.spacelab.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

import static com.example.spacelab.util.ValidationUtils.*;

@Component
@RequiredArgsConstructor
public class StudentValidator implements Validator {

    private final static String PHONE_PATTERN = "^([+]?[\\s0-9]+)?(\\d{3}|[(]?[0-9]+[)])?([-]?[\\s]?[0-9])+$";
    private final static String EMAIL_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private final static String TELEGRAM_PATTERN = "^@[A-Za-z0-9_.]{3,}$";

    private final TaskRepository taskRepository;
    private final StudentRepository studentRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Student.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors e) {
        StudentEditDTO dto = (StudentEditDTO) target;

        if(fieldIsEmpty(dto.firstName()))
            e.rejectValue("firstName", "firstName.empty", "validation.field.empty");
        else if(fieldMaxLengthIsIncorrect(dto.firstName(), 50))
            e.rejectValue("firstName", "firstName.length", "validation.field.length.max");

        if(fieldIsEmpty(dto.lastName()))
            e.rejectValue("lastName", "lastName.empty", "validation.field.empty");
        else if(fieldMaxLengthIsIncorrect(dto.lastName(), 50))
            e.rejectValue("lastName", "lastName.length", "validation.field.length.max");

//        if(fieldIsEmpty(dto.fathersName()))
//            e.rejectValue("fathersName", "fathersName.empty", "Enter fathers name!");
//        else if(dto.fathersName().length() < 2 || dto.fathersName().length() > 50)
//            e.rejectValue("fathersName", "fathersName.length", "validation.field.length.max");

        if(fieldIsNotEmpty(dto.fathersName()) && fieldMaxLengthIsIncorrect(dto.fathersName(), 50)) {
            e.rejectValue("fathersName", "fathersName.length", "validation.field.length.max");
        }

        if(fieldIsEmpty(dto.email()))
            e.rejectValue("email", "email.empty", "validation.field.empty");
        else if(!dto.email().matches(EMAIL_PATTERN))
            e.rejectValue("email", "email.no-match", "validation.field.format.allowed");
        else if(studentRepository.existsByDetailsEmail(dto.email())) {
            if(dto.id() == null)
                e.rejectValue("email", "email.taken", "validation.student.email.exists");
            else if(!studentRepository.findById(dto.id()).get().getDetails().getEmail().equals(dto.email()))
                e.rejectValue("email", "email.taken", "validation.student.email.exists");
        }

        if(fieldIsEmpty(dto.phone()))
            e.rejectValue("phone", "phone.empty", "validation.field.empty");
        else if(!dto.phone().matches(PHONE_PATTERN))
            e.rejectValue("phone", "phone.no-match", "validation.field.format.allowed");

//        if(dto.telegram() == null || dto.telegram().isEmpty())
//            e.rejectValue("telegram", "telegram.empty", "Enter telegram!");
//        else if(!dto.telegram().matches(TELEGRAM_PATTERN))
//            e.rejectValue("telegram", "telegram.no-match", "Incorrect telegram format!");

    }

    public void validateNewTaskForStudent(StudentTaskUnlockRequest request, BindingResult bindingResult) {
        Task task = taskRepository.findById(request.taskID()).orElse(null);
        if(task != null) {
            Student student = studentRepository.findById(request.studentID()).orElse(null);
            if(student != null) {
                if(task.getActiveStudents().contains(student)) {
                    bindingResult.addError(new FieldError("StudentTaskUnlockRequest", "taskID", "validation.student.task.already-present"));
                }
            } else {
                bindingResult.addError(new FieldError("StudentTaskUnlockRequest", "studentID", "validation.student.task.does-not-exist"));
            }
        } else {
            bindingResult.addError(new FieldError("StudentTaskUnlockRequest", "taskID", "validation.student.task.does-not-exist"));
        }
    }
}
