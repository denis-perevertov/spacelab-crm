package com.example.spacelab.dto.course;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseInformationDTO {

    @Schema(example = "CourseMainDescription")
    private String main_description;

    @Schema(example = "[\"topic1\",\"topic2\",\"topic3\"]")
    private List<String> topics;

    @Schema(example = "6-8 months")
    private String completionTime;

    @Schema(example = "15")
    private Integer groupSize;

    @Schema(example = "60")
    private Integer hoursNorm;


}
