package com.example.spacelab.mapper;

import com.example.spacelab.model.Course;
import com.example.spacelab.model.StudentTask;
import com.example.spacelab.model.Task;
import com.example.spacelab.model.dto.CourseDTO;
import com.example.spacelab.model.dto.StudentTaskDTO;
import com.example.spacelab.model.dto.TaskDTO;
import com.example.spacelab.repository.CourseRepository;
import com.example.spacelab.repository.StudentTaskRepository;
import com.example.spacelab.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskDTOMapper {

    private final TaskRepository taskRepository;
    private final CourseRepository courseRepository;

    public TaskDTO fromTaskToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setLevel(task.getLevel());
        dto.setStatus(task.getStatus());

        dto.setCourse(new CourseDTO());   // todo

        return dto;
    }

    public StudentTaskDTO fromStudentTaskToStudentTaskDTO(StudentTask studentTask) {
        StudentTaskDTO dto = new StudentTaskDTO();

        dto.setId(studentTask.getId());
        dto.setTask(fromTaskToDTO(studentTask.getTask()));
        dto.setBeginDate(studentTask.getBeginDate());
        dto.setEndDate(studentTask.getEndDate());
        dto.setStatus(studentTask.getStatus().toString());

        return dto;
    }


    public Task fromDTOToTask(TaskDTO dto) {
        if(dto.getId() != null && dto.getId() != 0) return taskRepository.getReferenceById(dto.getId());
        else {
            Task task = new Task();
            task.setId(null);
            task.setName(dto.getName());
            task.setLevel(dto.getLevel());
            task.setStatus(dto.getStatus());

            /*Course course = courseRepository.getReferenceById(dto.getCourse().getId());
            task.setCourse(course);*/

            if(dto.getCourse() != null) courseRepository.findById(dto.getCourse().getId()).ifPresent(task::setCourse);
            return task;
        }
    }
}
