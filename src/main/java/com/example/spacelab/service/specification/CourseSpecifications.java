package com.example.spacelab.service.specification;

import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.course.CourseStatus;
import com.example.spacelab.model.course.Course_;
import com.example.spacelab.model.student.Student;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class CourseSpecifications {

    public static Specification<Course> hasCourseIDs(Long... ids) {
        if(ids == null) return (root, query, cb) -> null;
        return (root, query, cb) -> root.in(Course_.ID, ids);
    }

    public static Specification<Course> hasNameLike(String name) {
        if(name == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.like(root.get(Course_.NAME), "%"+name+"%");
    }

    public static Specification<Course> hasDatesBetween(LocalDate from, LocalDate to) {
        if(from == null || to == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.or(
                cb.between(root.get(Course_.BEGINNING_DATE), from, to),
                cb.between(root.get(Course_.END_DATE), from, to)
        );
    }

    public static Specification<Course> hasMentor(Admin admin) {
        if(admin == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.equal(root.get(Course_.MENTOR), admin);
    }

    public static Specification<Course> hasManager(Admin admin) {
        if(admin == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.equal(root.get(Course_.MANAGER), admin);
    }

//    public static Specification<Course> hasActive(Boolean active) {
//        if(active == null) return (root, query, cb) -> null;
//        return (root, query, cb) -> cb.equal(root.get(Course_.ACTIVE), active);
//    }

    public static Specification<Course> hasStatus(CourseStatus status) {
        if(status == null) return ((root, query, criteriaBuilder) -> null);
        return (root, query ,cb) -> cb.equal(root.get(Course_.STATUS), status);
    }
}
