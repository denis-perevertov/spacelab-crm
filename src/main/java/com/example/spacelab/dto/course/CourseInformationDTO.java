package com.example.spacelab.dto.course;

import com.example.spacelab.model.course.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseInformationDTO {

    private String main_description;

    private List<String> topics;

    private String completionTime;

    private Integer groupSize;

    private Integer hoursNorm;


}
