package com.example.spacelab.validator;

import com.example.spacelab.dto.course.CourseSaveUpdatedDTO;
import com.example.spacelab.dto.literature.LiteratureSaveDTO;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.literature.Literature;
import com.example.spacelab.model.literature.LiteratureType;
import com.example.spacelab.repository.AdminRepository;
import com.example.spacelab.repository.CourseRepository;
import com.example.spacelab.repository.LiteratureRepository;
import com.example.spacelab.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

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

        System.out.println("dto: " + dto);

        if(dto.getName() == null || dto.getName().isEmpty())
            e.rejectValue("name", "name.empty", "validation.field.empty");
        else if(dto.getName().length() > 100)
            e.rejectValue("name", "name.length", "validation.field.length");

        if(dto.getType() == null)
            e.rejectValue("type", "type.empty", "validation.field.select");

        if(dto.getAuthor() == null || dto.getAuthor().isEmpty()) {
            e.rejectValue("author", "author.empty", "validation.field.empty");
        }
        else if(dto.getAuthor().length() > 100) {
            e.rejectValue("author", "author.length", "validation.field.length");
        }

        if(dto.getCourseID() == null || dto.getCourseID() <= 0) {
            e.rejectValue("courseID", "courseID.empty", "validation.field.select");
        }
        else if(!courseRepository.existsById(dto.getCourseID())) {
            e.rejectValue("courseID", "courseID.no-match", "Course with this ID doesn't exist!");
        }

        if(dto.getKeywords() != null && dto.getKeywords().length() > 200) {
            e.rejectValue("keywords", "keywords.length", "validation.field.length");
        }

        if(dto.getDescription() == null || dto.getDescription().isEmpty())
            e.rejectValue("description", "description.empty", "validation.field.empty");
        else if(dto.getDescription().length() > 1000)
            e.rejectValue("description", "description.length", "validation.field.length");

        if(dto.getType() == LiteratureType.LINK) {
            if(dto.getResource_link() == null || dto.getResource_link().isEmpty())
                e.rejectValue("resource_link", "resource_link.empty", "validation.field.empty");
            else if(dto.getResource_link().length() < 10 || dto.getResource_link().length() > 200)
                e.rejectValue("resource_link", "resource_link.length", "validation.field.length");
        }
        else if(dto.getType() == LiteratureType.BOOK) {
            String filename = dto.getResource_file().getOriginalFilename();
            assert filename != null;
            String extension = filename.substring(filename.lastIndexOf(".")+1);
            System.out.println("Size: " + dto.getResource_file().getSize());
            System.out.println("FileName: " + filename);
            if(dto.getResource_file().isEmpty()) {
                if(
                    dto.getId() == null
                    || Optional.ofNullable(literatureRepository.findById(dto.getId()).orElse(new Literature()).getResource_link()).isEmpty()
                ) {
                    e.rejectValue("resource_file", "resource_file.empty", "validation.file.upload");
                }
            }
            else if(dto.getResource_file().getSize() > ValidationUtils.MAX_FILE_SIZE) {
                e.rejectValue("resource_file", "resource_file.max-size", "validation.file.max-size");
            }
            else if(!extension.equalsIgnoreCase(ALLOWED_FORMAT)) {
                e.rejectValue("resource_file", "resource_file.max-size", "validation.file.max-size");
            }
        }

        MultipartFile thumbnail = dto.getThumbnail();
        if(thumbnail == null || thumbnail.isEmpty()) {
            if(
                    dto.getId() == null
                    || Optional.ofNullable(literatureRepository.findById(dto.getId()).orElse(new Literature()).getThumbnail()).isEmpty()
            ) {
                e.rejectValue("thumbnail", "thumbnail.empty", "validation.file.upload");
            }
        }
        else if(thumbnail.getSize() > ValidationUtils.MAX_IMAGE_SIZE) {
            e.rejectValue("thumbnail", "thumbnail.max-size", "validation.file.max-size");
        }
        else {
            String filename = dto.getThumbnail().getOriginalFilename();
            assert filename != null;
            String extension = filename.substring(filename.lastIndexOf(".")+1);
            if(!ValidationUtils.ALLOWED_IMAGE_FORMATS.contains(extension)) {
                e.rejectValue("thumbnail", "thumbnail.extension", "validation.file.extension.allowed");
            }
        }

    }
}
