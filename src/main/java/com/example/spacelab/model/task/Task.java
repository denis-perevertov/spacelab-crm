package com.example.spacelab.model.task;

import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.lesson.LessonReportRow;
import com.example.spacelab.model.literature.Literature;
import com.example.spacelab.model.student.Student;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@Table(name="tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_task_id")
    private Task parentTask;

    @ToString.Exclude
    @OneToMany(mappedBy = "parentTask")
    private List<Task> subtasks = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private int taskIndex;  // это просто дикая хрень, я хз

    private String name;

    @Enumerated(value = EnumType.STRING)
    private TaskLevel level;

    @Embedded
    private CompletionTime completionTime;

    @Column(columnDefinition = "TEXT")
    private String skillsDescription;

    @Column(columnDefinition = "TEXT")
    private String taskDescription;

    @ToString.Exclude
    @ManyToMany
    private List<Literature> recommendedLiterature = new ArrayList<>();

    @ToString.Exclude
    @ManyToMany
    private List<Student> activeStudents = new ArrayList<>();

    @ToString.Exclude
    @ManyToMany
    private List<LessonReportRow> lessonReportRows = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    private TaskStatus status;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<TaskProgressPoint> taskProgressPoints = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskIndex == task.taskIndex && Objects.equals(id, task.id) && Objects.equals(name, task.name) && level == task.level && Objects.equals(completionTime, task.completionTime) && Objects.equals(skillsDescription, task.skillsDescription) && Objects.equals(taskDescription, task.taskDescription) && status == task.status && Objects.equals(taskProgressPoints, task.taskProgressPoints) && Objects.equals(createdAt, task.createdAt) && Objects.equals(updatedAt, task.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taskIndex, name, level, completionTime, skillsDescription, taskDescription, status, taskProgressPoints, createdAt, updatedAt);
    }
}
