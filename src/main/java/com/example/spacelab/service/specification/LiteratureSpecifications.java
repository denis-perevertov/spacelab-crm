package com.example.spacelab.service.specification;

import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.literature.Literature;
import com.example.spacelab.model.literature.LiteratureType;
import org.springframework.data.jpa.domain.Specification;

public class LiteratureSpecifications {

    public static Specification<Literature> hasNameOrAuthorLike(String input) {
        if(input == null) return (root, query, cb) -> null;
        return (root, query, cb) ->
                cb.or(
                        cb.like(root.get("name"), "%"+input+"%"),
                        cb.like(root.get("author"), "%"+input+"%")
                );
    }

    public static Specification<Literature> hasCourse(Course course) {
        if(course == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.equal(root.get("course"), course);
    }

    public static Specification<Literature> hasType(LiteratureType type) {
        if(type == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.equal(root.get("type"), type);
    }

    public static Specification<Literature> hasKeywordsLike(String input) {
        if(input == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.like(root.get("keywords"), "%"+input+"%");
    }

}
