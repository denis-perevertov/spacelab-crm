package com.example.spacelab.service.specification;

import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.role.UserRole;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@RequiredArgsConstructor
public class AdminTestSpec implements Specification<Admin> {

    private final String email;
//    private final String telegram;
//    private final String phone;
//    private final UserRole role;
//    private final Course course;

    @Override
    public Predicate toPredicate(Root<Admin> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate where = cb.conjunction();
        if(email != null && !email.isEmpty()) where = cb.and(where, cb.like(root.get("email"), "%"+email+"%"));
        return where;
    }
}
