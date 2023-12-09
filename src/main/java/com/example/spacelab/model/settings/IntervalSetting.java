package com.example.spacelab.model.settings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum IntervalSetting {
    SEVEN_DAYS(7),
    TEN_DAYS(10),
    FIFTEEN_DAYS(15);

    private final Integer value;
}
