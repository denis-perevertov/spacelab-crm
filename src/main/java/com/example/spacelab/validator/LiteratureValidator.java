package com.example.spacelab.validator;

import com.example.spacelab.dto.course.CourseSaveUpdatedDTO;
import com.example.spacelab.dto.literature.LiteratureSaveDTO;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.literature.Literature;
import com.example.spacelab.model.literature.LiteratureType;
import com.example.spacelab.repository.AdminRepository;
import com.example.spacelab.repository.CourseRepository;
import com.example.spacelab.repository.LiteratureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class LiteratureValidator implements Validator {

    private static final String ALLOWED_FORMAT = "pdf";

    private final CourseRepository courseRepository;
    private final LiteratureRepository literatureRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Literature.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors e) {
        LiteratureSaveDTO dto = (LiteratureSaveDTO) target;

        System.out.println("dto: " + dto.toString());

        if(dto.getName() == null || dto.getName().isEmpty())
            e.rejectValue("name", "name.empty", "Enter name!");
        else if(dto.getName().length() < 2 || dto.getName().length() > 100)
            e.rejectValue("name", "name.length", "Name length: 2-100");

        if(dto.getType() == null)
            e.rejectValue("type", "type.empty", "Select type of Literature!");

        if(dto.getAuthor() == null || dto.getAuthor().isEmpty()) {
            e.rejectValue("author", "author.empty", "Enter author!");
        }
        else if(dto.getAuthor().length() < 2 || dto.getAuthor().length() > 100) {
            e.rejectValue("author", "author.length", "Author name length: 2-100");
        }

        if(dto.getCourseID() == null || dto.getCourseID() <= 0) {
            e.rejectValue("courseID", "courseID.empty", "Select course!");
        }
        else if(!courseRepository.existsById(dto.getCourseID())) {
            e.rejectValue("courseID", "courseID.no-match", "Course with this ID doesn't exist!");
        }

        if(dto.getKeywords() != null && dto.getKeywords().length() > 200) {
            e.rejectValue("keywords", "keywords.length", "Max total length of keywords is 200!");
        }

        if(dto.getDescription() == null || dto.getDescription().isEmpty())
            e.rejectValue("description", "description.empty", "Enter description!");
        else if(dto.getDescription().length() < 2 || dto.getDescription().length() > 300)
            e.rejectValue("description", "description.length", "Description length: 2-300");

        if(dto.getType() == LiteratureType.LINK) {
            if(dto.getResource_link() == null || dto.getResource_link().isEmpty())
                e.rejectValue("resource_link", "resource_link.empty", "Enter resource link!");
            else if(dto.getResource_link().length() < 10 || dto.getResource_link().length() > 200)
                e.rejectValue("resource_link", "resource_link.length", "Resource link length: 10-200");
        }
        else if(dto.getType() == LiteratureType.BOOK) {
            System.out.println("Size: " + dto.getResource_file().getSize());
            System.out.println("FileName: " + dto.getResource_file().getOriginalFilename());
            if(dto.getResource_file().getSize() < 1) {
                if(
                    dto.getId() == null
                    || literatureRepository.findById(dto.getId()).orElse(new Literature()).getResource_link().isEmpty()
                ) {
                    e.rejectValue("resource_file", "resource_file.empty", "Upload file!");
                }
            }
            else if(!dto.getResource_file().getOriginalFilename().split("\\.")[1].equals(ALLOWED_FORMAT)) {
                e.rejectValue("resource_file", "resource_file.extension", "Wrong file extension!");
            }
        }
    }
}
