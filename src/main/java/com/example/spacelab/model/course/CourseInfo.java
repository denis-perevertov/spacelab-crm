package com.example.spacelab.model.course;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

@Embeddable
public class CourseInfo {

    private String main_description;
    private List<String> topics;
    private String completionTime;
    private Integer groupSize;
    private Integer hoursNorm;
}
