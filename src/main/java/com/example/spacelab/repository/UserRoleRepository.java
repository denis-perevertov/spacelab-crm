package com.example.spacelab.repository;

import com.example.spacelab.model.role.UserRole;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Hidden
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    UserRole findByName(String name);
    boolean existsByName(String name);
    UserRole getReferenceByName(String name);
}
