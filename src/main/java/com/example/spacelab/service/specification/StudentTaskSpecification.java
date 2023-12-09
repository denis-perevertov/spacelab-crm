package com.example.spacelab.service.specification;

import com.example.spacelab.model.student.StudentTask;
import com.example.spacelab.model.student.StudentTaskStatus;
import com.example.spacelab.model.student.StudentTask_;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class StudentTaskSpecification implements Specification<StudentTask> {

    private final Long studentID;
    private final StudentTaskStatus status;
    private final Long taskID;
    private final String taskName;
    private final Long courseID;
    private final LocalDate beginDate;
    private final LocalDate endDate;

    @Override
    public Predicate toPredicate(Root<StudentTask> studentTask, CriteriaQuery<?> q, CriteriaBuilder cb) {

        System.out.println("BEGIN DATE: " + beginDate);
        System.out.println("END DATE: " + endDate);

        List<Predicate> predicates = new ArrayList<>();
        if(studentID != null)
            predicates.add(cb.equal(studentTask.get("student").get("id"), studentID));
        if(status != null)
            predicates.add(cb.equal(studentTask.get("status"), status));
        if(taskID != null)
            predicates.add(cb.equal(studentTask.get("id"), taskID));
        if(taskName != null)
            predicates.add(cb.like(cb.lower(studentTask.get("taskReference").get("name")), "%"+taskName.toLowerCase()+"%"));
        if(courseID != null)
            predicates.add(cb.equal(studentTask.get("taskReference").get("course").get("id"), courseID));

        if(beginDate != null && endDate != null)
            predicates.add(
                    cb.or(
                            cb.and(
                                    cb.lessThanOrEqualTo(studentTask.get(StudentTask_.BEGIN_DATE), beginDate),
                                    cb.greaterThanOrEqualTo(studentTask.get(StudentTask_.END_DATE), beginDate)
                            ),
                            cb.and(
                                    cb.lessThanOrEqualTo(studentTask.get(StudentTask_.BEGIN_DATE), endDate),
                                    cb.greaterThanOrEqualTo(studentTask.get(StudentTask_.END_DATE), endDate)
                            )
                    )
            );

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
