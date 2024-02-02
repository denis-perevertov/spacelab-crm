package com.example.spacelab.service.specification;

import com.example.spacelab.model.student.StudentTask;
import com.example.spacelab.model.student.StudentTaskStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.ZonedDateTime;

public class StudentTaskSpecifications {

    public static Specification<StudentTask> hasId(Long id) {
        if(id == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.equal(root.get("id"), id);
    }

    public static Specification<StudentTask> hasStudentId(Long id) {
        if(id == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.equal(root.get("student").get("id"), id);
    }

    public static Specification<StudentTask> hasNameLike(String name) {
        if(name == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.like(root.get("taskReference").get("name"), "%"+name+"%");
    }

    public static Specification<StudentTask> hasCourseID(Long id) {
        if(id == null || id < 0) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.equal(root.get("taskReference").get("course").get("id"), id);
    }

    public static Specification<StudentTask> hasDatesBetween(ZonedDateTime from, ZonedDateTime to) {
        if(from == null && to == null) return (root, query, cb) -> null;
        else if(from != null && to != null) return (root, query, cb) -> cb.or(
                cb.between(root.get("beginDate"), from, to),
                cb.between(root.get("endDate"), from, to)
        );
        else if(from != null) return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("beginDate"), from);
        else return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("beginDate"), to);
    }

    public static Specification<StudentTask> hasStatus(StudentTaskStatus status) {
        if(status == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }
}
