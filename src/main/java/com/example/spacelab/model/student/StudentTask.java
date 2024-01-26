package com.example.spacelab.model.student;

import com.example.spacelab.model.task.Task;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="student_tasks")
@Accessors(chain = true)
public class StudentTask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Task taskReference;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(nullable = false)
    private Student student;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parent_task_id")
    private StudentTask parentTask;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL)
    private List<StudentTask> subtasks = new ArrayList<>();

    private ZonedDateTime beginDate;
    private ZonedDateTime endDate;

    @Enumerated(value = EnumType.STRING)
    private StudentTaskStatus status = StudentTaskStatus.LOCKED;

    private Integer percentOfCompletion;

    private String taskTrackingId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentTask that = (StudentTask) o;
        return Objects.equals(id, that.id) && Objects.equals(beginDate, that.beginDate) && Objects.equals(endDate, that.endDate) && status == that.status && Objects.equals(percentOfCompletion, that.percentOfCompletion) && Objects.equals(taskTrackingId, that.taskTrackingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, beginDate, endDate, status, percentOfCompletion, taskTrackingId);
    }
}
