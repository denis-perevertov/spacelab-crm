package com.example.spacelab.model.settings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum HoursSetting {
    SIXTY_FIVE_HOURS(65),
    THIRTY_FIVE_HOURS(35),
    TWENTY_FIVE_HOURS(25);

    private final Integer value;
}
