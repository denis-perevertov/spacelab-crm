package com.example.spacelab.model.literature;

import com.example.spacelab.model.course.Course;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="literature")
public class Literature {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String author;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Enumerated(value = EnumType.STRING)
    private LiteratureType type;

    private String keywords;
    private String description;
    private String resource_link;  // filename for files, URL for links

    private String img;

    private Boolean is_verified;
}
