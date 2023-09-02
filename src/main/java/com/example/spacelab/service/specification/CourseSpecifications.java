package com.example.spacelab.service.specification;

import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.student.Student;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class CourseSpecifications {

    public static Specification<Course> hasCourseIDs(Long... ids) {
        if(ids == null) return (root, query, cb) -> null;
        return (root, query, cb) -> root.in("id", ids);
    }

    public static Specification<Course> hasNameLike(String name) {
        if(name == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.like(root.get("name"), "%"+name+"%");
    }

    public static Specification<Course> hasDatesBetween(LocalDate from, LocalDate to) {
        if(from == null || to == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.or(
                cb.between(root.get("beginDate"), from, to),
                cb.between(root.get("endDate"), from, to)
        );
    }

    public static Specification<Course> hasMentor(Admin admin) {
        if(admin == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.equal(root.get("mentor"), admin);
    }

    public static Specification<Course> hasManager(Admin admin) {
        if(admin == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.equal(root.get("manager"), admin);
    }

    public static Specification<Course> hasActive(Boolean active) {
        if(active == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.equal(root.get("isActive"), active);
    }
}
