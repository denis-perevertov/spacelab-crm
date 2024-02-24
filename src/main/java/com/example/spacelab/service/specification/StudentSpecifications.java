package com.example.spacelab.service.specification;

import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.student.StudentAccountStatus;
import com.example.spacelab.model.student.StudentDetails_;
import com.example.spacelab.model.student.Student_;
import jakarta.persistence.criteria.Join;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public class StudentSpecifications {

    public static Specification<Student> hasId(Long id) {
        if(id == null) return (root,query,cb) -> null;
        return (root, query, cb) -> cb.equal(root.get("id"), id);
    }

    public static Specification<Student> hasNameOrEmailLike(String input) {
        if(input == null) return (root, query, cb) -> null;
        return (root, query, cb) ->
            cb.or(
                    cb.like(
                            cb.concat(
                                    cb.concat(root.get(Student_.DETAILS).get(StudentDetails_.FIRST_NAME), StringUtils.SPACE),
                                    cb.lower(root.get(Student_.DETAILS).get(StudentDetails_.LAST_NAME))
                            ),
                            "%"+input+"%"
                    ),
                    cb.like(root.get(Student_.DETAILS).get(StudentDetails_.EMAIL),"%"+input+"%")
            );

    }

    public static Specification<Student> hasCourse(Course course) {
        if(course == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.equal(root.get(Student_.COURSE), course);
    }

    public static Specification<Student> hasCourseIDs(Long... ids) {
        if(ids == null) return (root, query, cb) -> null;
        return (root, query, cb) -> {
            Join<Course, Student> csj = root.join(Student_.COURSE);
            return root.get(Student_.COURSE).get("id").in(ids);
        };
    }

    public static Specification<Student> hasPhoneLike(String phone) {
        if(phone == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.like(root.get(Student_.DETAILS).get(StudentDetails_.PHONE), "%"+phone+"%");
    }

    public static Specification<Student> hasTelegramLike(String telegram) {
        if(telegram == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.like(root.get(Student_.DETAILS).get(StudentDetails_.TELEGRAM), "%"+telegram+"%");
    }

    public static Specification<Student> hasRatingOrHigher(Integer rating) {
        if(rating == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(Student_.RATING), rating);
    }

    public static Specification<Student> hasRatingBetween(Integer from, Integer to) {
        if(from == null && to == null) return (root, query, cb) -> null;
        else if(from == null) return (root, query, cb) -> cb.and(
                cb.isNotNull(root.get(Student_.RATING)),
                cb.lessThanOrEqualTo(root.get(Student_.RATING), to)
        );
        else if(to == null) return (root, query, cb) -> cb.and(
                cb.isNotNull(root.get(Student_.RATING)),
                cb.greaterThanOrEqualTo(root.get(Student_.RATING), from)
        );
        return (root, query, cb) -> cb.and(
                cb.isNotNull(root.get(Student_.RATING)),
                cb.between(root.get(Student_.RATING), from, to)
        );
    }

    public static Specification<Student> hasStatus(StudentAccountStatus status) {
        if(status == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.equal(root.get(Student_.DETAILS).get(StudentDetails_.ACCOUNT_STATUS), status);
    }

    public static Specification<Student> isAvailable() {
        return (root, query, cb) -> cb.or(
                cb.equal(root.get(Student_.DETAILS).get(StudentDetails_.ACCOUNT_STATUS), StudentAccountStatus.ACTIVE),
                cb.equal(root.get(Student_.DETAILS).get(StudentDetails_.ACCOUNT_STATUS), StudentAccountStatus.INACTIVE),
                cb.equal(root.get(Student_.DETAILS).get(StudentDetails_.ACCOUNT_STATUS), StudentAccountStatus.HIRED)
        );
    }

}
