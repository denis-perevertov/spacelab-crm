package com.example.spacelab.model.task;

import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.lesson.LessonReportRow;
import com.example.spacelab.model.literature.Literature;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="tasks")
@NoArgsConstructor
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
    private List<TaskProgressPoint> taskProgressPoints = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Task(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
