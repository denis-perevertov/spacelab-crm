package com.example.spacelab.service.specification;

import com.example.spacelab.model.lesson.LessonReportRow;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class LessonReportSpecifications {

    public static Specification<LessonReportRow> hasStudentId(Long studentId) {
        if(studentId == null || studentId < 0) return ((root, query, criteriaBuilder) -> null);
        else return (root, query, cb) -> cb.equal(root.get("student").get("id"), studentId);
    }

    public static Specification<LessonReportRow> wasPresent(Boolean present) {
        if(present == null) return ((root, query, criteriaBuilder) -> null);
        else return (root, query, cb) -> cb.equal(root.get("wasPresent"), present);
    }

//    static Specification<LessonReportRow> hasTaskId(Long taskId) {
//        if(taskId == null || taskId < 0) return ((root, query, criteriaBuilder) -> null);
//        else return (root, query, cb) -> cb.equal(root.get("student").get("id"), taskId);
//    }

    public static Specification<LessonReportRow> hasDatesBetween(LocalDateTime from, LocalDateTime to) {
        if(from == null && to == null) return ((root, query, criteriaBuilder) -> null);
        else if(from != null && to != null) return (root, query, cb) -> cb.between(root.get("lesson").get("datetime"), from, to);
        else if(from != null) return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("lesson").get("datetime"), from);
        else return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("lesson").get("datetime"), to);
    }

    public static Specification<LessonReportRow> hasHoursBetween(Integer from, Integer to) {
        if(from == null && to == null) return ((root, query, criteriaBuilder) -> null);
        else if(from != null && to != null) return (root, query, cb) -> cb.between(root.get("hours"), from, to);
        else if(from != null) return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("hours"), from);
        else return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("hours"), to);
    }

    public static Specification<LessonReportRow> hasNoteLike(String note) {
        if(note == null || note.isEmpty()) return (root, query, cb) -> null;
        else return (root, query , cb ) -> cb.like(root.get("hoursNote"), "%"+note+"%");
    }
    public static Specification<LessonReportRow> hasCommentLike(String comment) {
        if(comment == null || comment.isEmpty()) return (root, query, cb) -> null;
        else return (root, query , cb ) -> cb.like(root.get("comment"), "%"+comment+"%");
    }

    public static Specification<LessonReportRow> hasRatingBetween(Integer from, Integer to) {
        if(from == null && to == null) return ((root, query, criteriaBuilder) -> null);
        else if(from != null && to != null) return (root, query, cb) -> cb.between(root.get("rating"), from, to);
        else if(from != null) return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("rating"), from);
        else return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("rating"), to);
    }

}
