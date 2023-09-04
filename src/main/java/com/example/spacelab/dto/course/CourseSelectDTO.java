package com.example.spacelab.dto.course;

import com.example.spacelab.model.course.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseSelectDTO {

    private Long id;

    private String name;

}
