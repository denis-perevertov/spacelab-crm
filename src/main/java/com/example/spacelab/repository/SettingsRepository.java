package com.example.spacelab.repository;

import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.settings.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SettingsRepository extends JpaRepository<Settings, Long> {
    Optional<Settings> findByAdmin(Admin admin);
}
