package com.example.spacelab.dto.course;

import com.example.spacelab.dto.task.TaskCourseDTO;
import com.example.spacelab.model.task.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseTaskStructureDTO {

    private List<TaskCourseDTO> tasks = new ArrayList<>();


}
