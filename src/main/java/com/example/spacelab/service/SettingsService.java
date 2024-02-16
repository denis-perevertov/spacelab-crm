package com.example.spacelab.service;

import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.settings.Settings;
import com.example.spacelab.util.EnumResponse;
import io.swagger.v3.oas.annotations.Hidden;

import java.util.List;
@Hidden
public interface SettingsService {

    Long getSettingsIdForAdmin(Admin admin);
    Settings getSettingsForAdmin(Admin admin);
    Settings createDefaultSettings(Admin admin);
    Settings saveSettingsForAdmin(Settings settingsToSave);

    List<EnumResponse> getThemeOptions();
    List<EnumResponse> getLanguageOptions();
    List<Integer> getHoursNormOptions();
    List<Integer> getIntervalOptions();


}
