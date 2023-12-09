package com.example.spacelab.model.settings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum LanguageSetting {
    UK("Українська"),
    RU("Русский"),
    EN("English(US)");

    private final String value;
}
