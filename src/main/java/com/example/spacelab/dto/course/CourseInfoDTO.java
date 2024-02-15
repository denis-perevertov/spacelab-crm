package com.example.spacelab.dto.course;

import com.example.spacelab.model.course.CourseStatus;
import com.example.spacelab.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CourseInfoDTO {

    private String description;
    private List<String> topics;
    private CourseSettingsDTO settings;

    public CourseInfoDTO(String description, List<String> topics, CourseSettingsDTO settings) {
        this.description = StringUtils.trimString(description);
        this.topics = topics;
        this.settings = settings;
    }
}
