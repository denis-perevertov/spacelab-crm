package com.example.spacelab.dto.task;

import com.example.spacelab.model.task.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SubtaskDTO {
    private Long id;
    private String name;
    private TaskStatus status;
}
