package com.example.spacelab.model.settings;

import com.example.spacelab.model.admin.Admin;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "settings")
@NoArgsConstructor
@AllArgsConstructor
public class Settings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Admin admin;

    private ThemeSetting theme;

    private LanguageSetting language;

    private Integer weeklyHoursNormSetting;

    private Integer standardIntervalSetting;

    private boolean automaticLessonStartSetting;

    private boolean automaticLessonCreationSetting;

    private boolean soundNotificationSetting;
}
