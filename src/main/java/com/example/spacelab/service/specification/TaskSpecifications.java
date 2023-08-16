package com.example.spacelab.service.specification;

import com.example.spacelab.model.Course;
import com.example.spacelab.model.Task;
import com.example.spacelab.util.TaskLevel;
import com.example.spacelab.util.TaskStatus;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecifications {

    public static Specification<Task> hasCourse(Course course) {
        if(course == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.equal(root.get("course"), course);
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
}
