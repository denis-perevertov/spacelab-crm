package com.example.spacelab.model.course;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@Data
public class CourseInfo {

    @Column(name="description", columnDefinition = "TEXT")
    private String main_description = "Empty description";
    private List<String> topics = new ArrayList<>();
    private String completionTime = "Unspecified time";
    private Integer groupSize = -1;
    private Integer hoursNorm = -1;
}
