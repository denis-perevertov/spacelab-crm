package com.example.spacelab.repository;

import com.example.spacelab.model.student.StudentInviteRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InviteStudentRequestRepository extends JpaRepository<StudentInviteRequest, String> {
}
