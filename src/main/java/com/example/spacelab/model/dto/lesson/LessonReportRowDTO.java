package com.example.spacelab.model.dto.lesson;

import com.example.spacelab.model.Lesson;
import com.example.spacelab.model.Student;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LessonReportRowDTO {

    private Long id;

    private String student;

    private Boolean wasPresent;

    private List<String> currentTasks;

    private Double hours;

    private String hoursNote;

    private String comment;

    private Integer rating;
}
