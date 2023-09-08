package com.example.spacelab.dto.course;

import com.example.spacelab.model.course.CourseStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class CourseSaveCreatedDTO {

    private Long id;

    private String name;

    private LocalDate beginDate;

    private Long mentorID;

    private Long managerID;

    public CourseSaveCreatedDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
