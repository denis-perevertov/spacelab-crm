package com.example.spacelab.service.specification;

import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.task.Task;
import com.example.spacelab.model.task.TaskLevel;
import com.example.spacelab.model.task.TaskStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Order;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecifications {

    public static Specification<Task> hasId(Long id) {
        if(id == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.equal(root.get("id"), id);
    }

    public static Specification<Task> hasCourse(Course course) {
        if(course == null) return (root, query, cb) -> null;
        return (root, query, cb) -> {
            query.orderBy(cb.asc(root.get("id")));
            return cb.equal(root.get("course"), course);
        };
    }

    public static Specification<Task> hasCourseIDs(Long... ids) {
        if(ids == null) return (root, query, cb) -> null;
        return (root, query, cb) -> {
            Join<Course, Task> ctj = root.join("course");
            return ctj.in("id", ids);
        };
    }

    public static Specification<Task> hasNameLike(String name) {
        if(name == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.like(root.get("name"), "%"+name+"%");
    }

    public static Specification<Task> hasLevel(TaskLevel level) {
        if(level == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.equal(root.get("level"), level);
    }

    public static Specification<Task> hasStatus(TaskStatus status) {
        if(status == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<Task> parentTaskIsNull() {
        return (root, query, cb) -> cb.isNull(root.get("parentTask"));
    }

    public static Specification<Task> courseIsNull() {
        return (root, query, cb) -> cb.isNull(root.get("course"));
    }

    public static Specification<Task> noCourseFirst() {
        return (root, query, cb) -> {
            query.orderBy(
                    cb.asc(cb.selectCase()
                            .when(cb.isNull(root.get("course")), 0)
                            .otherwise(1))
            );
            return cb.isNotNull(root.get("id"));
        };
    }

    public static Specification<Task> tasksWithCoursesFirst() {
        return (root, query, cb) -> {
            query.orderBy(
                    cb.desc(cb.selectCase()
                            .when(cb.isNull(root.get("course")), 0)
                            .otherwise(1))
            );
            return cb.isNotNull(root.get("id"));
        };
    }

}
