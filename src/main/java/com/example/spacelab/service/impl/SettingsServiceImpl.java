package com.example.spacelab.service.impl;

import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.settings.*;
import com.example.spacelab.repository.SettingsRepository;
import com.example.spacelab.service.SettingsService;
import com.example.spacelab.util.EnumResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@Log
@RequiredArgsConstructor
public class SettingsServiceImpl implements SettingsService {

    private final SettingsRepository settingsRepository;


    @Override
    public Long getSettingsIdForAdmin(Admin admin) {
        return settingsRepository.findByAdmin(admin).orElse(new Settings()).getId();
    }

    @Override
    public Settings getSettingsForAdmin(Admin admin) {
        return settingsRepository.findByAdmin(admin).orElse(new Settings());
    }

    @Override
    public Settings saveSettingsForAdmin(Settings settingsToSave) {
       return settingsRepository.save(settingsToSave);
    }

    @Override
    public List<EnumResponse> getThemeOptions() {
        return Arrays.stream(ThemeSetting.values()).map(theme -> new EnumResponse(theme.name(), theme.getValue())).toList();
    }

    @Override
    public List<EnumResponse> getLanguageOptions() {
        return Arrays.stream(LanguageSetting.values()).map(lang -> new EnumResponse(lang.name(), lang.getValue())).toList();
    }

    @Override
    public List<Integer> getHoursNormOptions() {
        return Arrays.stream(HoursSetting.values()).map(HoursSetting::getValue).toList();
    }

    @Override
    public List<Integer> getIntervalOptions() {
        return Arrays.stream(IntervalSetting.values()).map(IntervalSetting::getValue).toList();
    }

    @Override
    public Settings createDefaultSettings(Admin admin) {
        Settings settings =
                new Settings()
                        .setAdmin(admin)
                        .setTheme(ThemeSetting.DARK)
                        .setLanguage(LanguageSetting.UK)
                        .setWeeklyHoursNormSetting(35)
                        .setStandardIntervalSetting(7)
                        .setAutomaticLessonStartSetting(true)
                        .setAutomaticLessonCreationSetting(true)
                        .setSoundNotificationSetting(true);
        return settingsRepository.save(settings);
    }

}
