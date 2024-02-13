package com.example.spacelab.model.settings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum HoursSetting {
    SIXTY_FIVE_HOURS(65),
    FORTY_HOURS(40),
    THIRTY_FIVE_HOURS(35),
    TWENTY_FIVE_HOURS(25),
    TEN_HOURS(10);

    private final Integer value;
}
