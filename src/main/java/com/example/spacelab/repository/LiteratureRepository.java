package com.example.spacelab.repository;

import com.example.spacelab.model.Literature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LiteratureRepository extends JpaRepository<Literature, Long> {
}
