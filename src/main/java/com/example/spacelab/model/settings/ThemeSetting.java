package com.example.spacelab.model.settings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ThemeSetting {
    LIGHT("Світла"),
    DARK("Темна");

    private final String value;
}
