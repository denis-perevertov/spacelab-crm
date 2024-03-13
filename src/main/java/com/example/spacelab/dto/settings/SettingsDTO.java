package com.example.spacelab.dto.settings;

import com.example.spacelab.model.settings.LanguageSetting;
import com.example.spacelab.model.settings.ThemeSetting;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettingsDTO {

    private ThemeSetting theme;
    private LanguageSetting language;
    private Integer weeklyHoursNormSetting;
    private Integer standardIntervalSetting;
    private boolean automaticLessonStartSetting;
    private boolean automaticLessonCreationSetting;
    private boolean soundNotificationSetting;
}
