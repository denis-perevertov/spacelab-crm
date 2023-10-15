package com.example.spacelab.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Locale;

@Data
@AllArgsConstructor
public class ProgramDuration {

    String value;
    TimeUnit unit;
    
    public ProgramDuration(String completionTime) {
        this.value = completionTime.split(" ")[0];
        this.unit = TimeUnit.valueOf(completionTime.split(" ")[1].toUpperCase(Locale.ROOT));
    }

    public String getDurationString() {
        return this.value + ' ' + unit.name();
    }
}
