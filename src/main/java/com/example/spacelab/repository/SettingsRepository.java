package com.example.spacelab.repository;

import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.settings.Settings;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
@Hidden
public interface SettingsRepository extends JpaRepository<Settings, Long> {
    Optional<Settings> findByAdmin(Admin admin);
}
