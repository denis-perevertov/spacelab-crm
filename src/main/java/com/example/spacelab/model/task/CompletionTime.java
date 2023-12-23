package com.example.spacelab.model.task;

import com.example.spacelab.util.TimeUnit;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Embeddable
@Accessors(chain = true)
public class CompletionTime {
    private String value;
    private TimeUnit timeUnit;
}
