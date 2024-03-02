package com.example.spacelab.controller;

import com.example.spacelab.api.SettingsAPI;
import com.example.spacelab.dto.settings.SettingsDTO;
import com.example.spacelab.mapper.SettingsMapper;
import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.settings.Settings;
import com.example.spacelab.service.SettingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingsController implements SettingsAPI {

    private final SettingsService service;
    private final SettingsMapper mapper;

    @GetMapping
    public ResponseEntity<?> getLoggedInAdminSettings(@AuthenticationPrincipal Admin admin) {
        log.info("admin: " + admin);
        Settings settings = service.getSettingsForAdmin(admin);
        log.info("settings: " + settings);
        return ResponseEntity.ok().body(mapper.toDTO(settings));
    }

    @GetMapping("/get-themes")
    public ResponseEntity<?> getAllThemeOptions() {
        log.info("getting all theme options");
        return ResponseEntity.ok().body(service.getThemeOptions());
    }

    @GetMapping("/get-languages")
    public ResponseEntity<?> getAllLanguageOptions() {
        log.info("getting all language options");
        return ResponseEntity.ok().body(service.getLanguageOptions());
    }

    @GetMapping("/get-hours-norms")
    public ResponseEntity<?> getAllHoursNormValues() {
        log.info("getting all hours options");
        return ResponseEntity.ok().body(service.getHoursNormOptions());
    }

    @GetMapping("/get-intervals")
    public ResponseEntity<?> getAllIntervalValues() {
        log.info("getting all interval options");
        return ResponseEntity.ok().body(service.getIntervalOptions());
    }

    @PostMapping
    public ResponseEntity<?> saveSettingsForLoggedInAdmin(@AuthenticationPrincipal Admin admin,
                                                           @RequestBody SettingsDTO dto) {
        log.info("admin: " + admin);
        log.info("settings: " + dto);
        Settings settings = mapper.toEntity(dto);
        settings.setId(service.getSettingsIdForAdmin(admin));
        settings.setAdmin(admin);
        service.saveSettingsForAdmin(settings);
        return ResponseEntity.ok().body(mapper.toDTO(settings));
    }
}
