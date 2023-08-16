package com.example.spacelab.service.specification;

import com.example.spacelab.model.Admin;
import com.example.spacelab.model.Course;
import com.example.spacelab.model.Lesson;
import com.example.spacelab.model.Literature;
import com.example.spacelab.util.LessonStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class LessonSpecifications {

    public static Specification<Lesson> hasDatesBetween(LocalDate from, LocalDate to) {
        if(from == null || to == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.between(root.get("date"), from, to);
    }

    public static Specification<Lesson> hasCourse(Course course) {
        if(course == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.equal(root.get("course"), course);
    }

    public static Specification<Lesson> hasMentor(Admin admin) {
        if(admin == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.equal(root.get("mentor"), admin);
    }

    public static Specification<Lesson> hasManager(Admin admin) {
        if(admin == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.equal(root.get("manager"), admin);
    }

    public static Specification<Lesson> hasStatus(LessonStatus status) {
        if(status == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }
}
