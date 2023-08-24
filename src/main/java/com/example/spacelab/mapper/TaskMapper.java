package com.example.spacelab.mapper;

import com.example.spacelab.exception.MappingException;
import com.example.spacelab.model.student.StudentTask;
import com.example.spacelab.model.task.Task;
import com.example.spacelab.dto.CourseDTO;
import com.example.spacelab.dto.student.StudentTaskDTO;
import com.example.spacelab.dto.TaskDTO;
import com.example.spacelab.repository.CourseRepository;
import com.example.spacelab.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

@Component
@Log
@RequiredArgsConstructor
public class TaskMapper {

    private final TaskRepository taskRepository;
    private final CourseRepository courseRepository;

    public TaskDTO fromTaskToDTO(Task task) {
        TaskDTO dto = new TaskDTO();

        try {
            dto.setId(task.getId());
            dto.setName(task.getName());
            dto.setLevel(task.getLevel());
            dto.setStatus(task.getStatus());

            dto.setCourse(new CourseDTO());   // todo

        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("DTO: " + dto);
            throw new MappingException(e.getMessage());
        }


        return dto;
    }

    public StudentTaskDTO fromStudentTaskToDTO(StudentTask studentTask) {
        StudentTaskDTO dto = new StudentTaskDTO();

        try {
            dto.setId(studentTask.getId());
            dto.setTaskID(studentTask.getId());
            dto.setBeginDate(studentTask.getBeginDate());
            dto.setEndDate(studentTask.getEndDate());
            dto.setStatus(studentTask.getStatus().toString());

        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("DTO: " + dto);
            throw new MappingException(e.getMessage());

        }

        return dto;

    }


    // ??????
    public Task fromDTOToTask(TaskDTO dto) {
        if(dto.getId() != null && dto.getId() != 0) return taskRepository.getReferenceById(dto.getId());
        else {

            Task task = new Task();

            try {

                task.setId(null);
                task.setName(dto.getName());
                task.setLevel(dto.getLevel());
                task.setStatus(dto.getStatus());

            /*Course course = courseRepository.getReferenceById(dto.getCourse().getId());
            task.setCourse(course);*/

                if(dto.getCourse() != null) courseRepository.findById(dto.getCourse().getId()).ifPresent(task::setCourse);
            } catch (Exception e) {
                log.severe("Mapping error: " + e.getMessage());
                log.warning("Entity: " + task);
                throw new MappingException(e.getMessage());

            }

            return task;
        }
    }
}
