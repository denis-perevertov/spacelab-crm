package com.example.spacelab.model.dto.lesson;

import com.example.spacelab.model.Admin;
import com.example.spacelab.model.Course;
import com.example.spacelab.model.LessonReport;
import com.example.spacelab.util.LessonStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
@Data
public class LessonListDTO {

    private Long id;

    private LocalDateTime datetime;

    private Long courseId;
    private String courseName;

    private String link;
    private String status;

    private Long mentorId;
    private String mentorName;

    private Long managerId;
    private String managerName;

    private String presentStudentsQuantity;




}
