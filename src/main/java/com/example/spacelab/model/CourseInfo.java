package com.example.spacelab.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

@Embeddable
public class CourseInfo {

    private String main_description;

    private List<String> topics;

    private String completion_time;

    private Integer group_size;

    private Integer hours_norm;
}
