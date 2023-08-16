package com.example.spacelab.repository;

import com.example.spacelab.model.InviteStudentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InviteStudentRequestRepository extends JpaRepository<InviteStudentRequest, String> {
}
