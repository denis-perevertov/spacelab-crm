package com.example.spacelab.mapper;

import com.example.spacelab.dto.settings.SettingsDTO;
import com.example.spacelab.model.settings.Settings;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

@Component
@Log
@RequiredArgsConstructor
public class SettingsMapper {

    public SettingsDTO toDTO(Settings settings) {

        return new SettingsDTO(
                settings.getTheme(),
                settings.getLanguage(),
                settings.getWeeklyHoursNormSetting(),
                settings.getStandardIntervalSetting(),
                settings.isAutomaticLessonStartSetting(),
                settings.isSoundNotificationSetting(),
                settings.isAutomaticLessonCreationSetting()
        );
    }

    public Settings toEntity(SettingsDTO dto) {
        return  Settings.builder()
                .theme(dto.getTheme())
                .language(dto.getLanguage())
                .weeklyHoursNormSetting(dto.getWeeklyHoursNormSetting())
                .standardIntervalSetting(dto.getStandardIntervalSetting())
                .automaticLessonStartSetting(dto.isAutomaticLessonStartSetting())
                .soundNotificationSetting(dto.isSoundNotificationSetting())
                .automaticLessonCreationSetting(dto.isAutomaticLessonCreationSetting())
                .build();
    }
}
