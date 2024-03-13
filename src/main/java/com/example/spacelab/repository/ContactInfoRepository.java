package com.example.spacelab.repository;

import com.example.spacelab.model.contact.ContactInfo;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Hidden
@Repository
public interface ContactInfoRepository extends JpaRepository<ContactInfo, Long> {
}
