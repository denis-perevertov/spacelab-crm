package com.example.spacelab.repository;

import com.example.spacelab.model.RefreshToken;
import com.example.spacelab.model.admin.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByAdmin(Admin admin);
    Boolean existsByAdmin(Admin admin);
    void deleteByAdmin(Admin admin);
}
