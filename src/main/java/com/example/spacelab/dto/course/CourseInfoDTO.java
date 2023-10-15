package com.example.spacelab.dto.course;

import com.example.spacelab.model.course.CourseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseInfoDTO {

    private String description;
    private List<String> topics;
    private CourseSettingsDTO settings;
}
