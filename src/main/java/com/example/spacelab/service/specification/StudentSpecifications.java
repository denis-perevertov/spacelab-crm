package com.example.spacelab.service.specification;

import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.course.Course_;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.student.StudentAccountStatus;
import com.example.spacelab.model.student.StudentDetails_;
import com.example.spacelab.model.student.Student_;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class StudentSpecifications {

    public static Specification<Student> hasNameOrEmailLike(String input) {
        if(input == null) return (root, query, cb) -> null;
        return (root, query, cb) ->
            cb.or(
                    cb.like(root.get(Student_.DETAILS).get(StudentDetails_.FIRST_NAME), "%"+input+"%"),
                    cb.like(root.get(Student_.DETAILS).get(StudentDetails_.FATHERS_NAME), "%"+input+"%"),
                    cb.like(root.get(Student_.DETAILS).get(StudentDetails_.LAST_NAME), "%"+input+"%"),
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
            System.out.println(root.get(Student_.COURSE).get("id").toString());
            System.out.println(csj.get(Course_.ID));
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

    public static Specification<Student> hasStatus(StudentAccountStatus status) {
        if(status == null) return (root, query, cb) -> null;
        return (root, query, cb) -> cb.equal(root.get(Student_.DETAILS).get(StudentDetails_.ACCOUNT_STATUS), status);
    }

}
