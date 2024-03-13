package com.example.spacelab.model.course;

import com.example.spacelab.util.TimeUnit;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Embeddable
@Data
public class CourseInfo {

    @Column(name="description", columnDefinition = "TEXT")
    private String main_description = "Empty description";
    private List<String> topics = new ArrayList<>();
    private LocalDate beginDate;
    private String completionTime = "Unspecified time";
    private TimeUnit completionTimeUnit;
    private Integer groupSize = -1;
    private Integer hoursNorm = -1;
    private Integer lessonInterval = -1;
}
