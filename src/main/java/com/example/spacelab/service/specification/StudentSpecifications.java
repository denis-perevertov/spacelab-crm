package com.example.spacelab.service.specification;

import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.student.StudentAccountStatus;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class StudentSpecifications {

    public static Specification<Student> hasNameOrEmailLike(String input) {
        if(input == null) return (root, query, cb) -> null;
        return (root, query, cb) ->
            cb.or(
                    cb.like(root.get("firstName"), "%"+input+"%"),
                    cb.like(root.get("fathersName"), "%"+input+"%"),
                    cb.like(root.get("lastName"), "%"+input+"%"),
                    cb.like(root.get("email"),"%"+input+"%")
            );

    }

    public static Specification<Student> hasCourse(Course course) {
        if(course == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.equal(root.get("course"), course);
    }

    public static Specification<Student> hasCourseIDs(Long... ids) {
        if(ids == null) return (root, query, cb) -> null;
        return (root, query, cb) -> {
            Join<Course, Student> csj = root.join("course");
            System.out.println(root.get("course").get("id").toString());
            System.out.println(csj.get("id"));
            return root.get("course").get("id").in(ids);
        };
    }

    public static Specification<Student> hasPhoneLike(String phone) {
        if(phone == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.like(root.get("phone"), "%"+phone+"%");
    }

    public static Specification<Student> hasTelegramLike(String telegram) {
        if(telegram == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.like(root.get("telegram"), "%"+telegram+"%");
    }

    public static Specification<Student> hasRatingOrHigher(Integer rating) {
        if(rating == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("rating"), rating);
    }

    public static Specification<Student> hasStatus(StudentAccountStatus status) {
        if(status == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

}
