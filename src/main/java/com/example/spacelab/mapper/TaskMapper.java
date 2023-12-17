package com.example.spacelab.mapper;

import com.example.spacelab.dto.task.*;
import com.example.spacelab.exception.MappingException;
import com.example.spacelab.dto.student.StudentTaskDTO;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.literature.Literature;
import com.example.spacelab.model.student.Student;
import com.example.spacelab.model.student.StudentTask;
import com.example.spacelab.model.task.Task;
import com.example.spacelab.repository.CourseRepository;
import com.example.spacelab.repository.LiteratureRepository;
import com.example.spacelab.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Log
@RequiredArgsConstructor
public class TaskMapper {

    private final TaskRepository taskRepository;
    private final CourseRepository courseRepository;
    private final LiteratureRepository literatureRepository;

    private final StudentMapper studentMapper;


    public TaskListDTO fromTaskToListDTO(Task task) {
        TaskListDTO dto = new TaskListDTO();

        try {
            dto.setId(task.getId());
            dto.setName(task.getName());
            dto.setLevel(task.getLevel());
            dto.setStatus(task.getStatus());
            dto.setCourseID(task.getCourse().getId());
            dto.setCourseName(task.getCourse().getName());

        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("DTO: " + dto);
            throw new MappingException(e.getMessage());
        }


        return dto;
    }

    public List<TaskListDTO> fromTaskListToDTOlist(List<Task> taskList) {
        List<TaskListDTO> dtoList = new ArrayList<>();

        try {
            for (Task task : taskList) {
                dtoList.add(fromTaskToListDTO(task));
            }
        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("DTO: " + dtoList);
            throw new MappingException(e.getMessage());
        }


        return dtoList;
    }

    public Page<TaskListDTO> fromTaskPageToDTOPage(Page<Task> taskList) {
        List<TaskListDTO> dtoList = new ArrayList<>();
        Page<TaskListDTO> dtoPage;

        try {
            for (Task task : taskList.getContent()) {
                dtoList.add(fromTaskToListDTO(task));
            }
            dtoPage = new PageImpl<>(dtoList, taskList.getPageable(), taskList.getTotalElements());
        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("DTO: " + dtoList);
            throw new MappingException(e.getMessage());
        }

        return dtoPage;
    }

    // ??????
    public Task fromListDTOToTask(TaskListDTO dto) {
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

//                if(dto.getCourse() != null) courseRepository.findById(dto.getCourse().getId()).ifPresent(task::setCourse);
            } catch (Exception e) {
                log.severe("Mapping error: " + e.getMessage());
                log.warning("Entity: " + task);
                throw new MappingException(e.getMessage());
            }
            return task;
        }
    }


    public StudentTaskDTO fromStudentTaskToDTO(StudentTask studentTask) {
        StudentTaskDTO dto = new StudentTaskDTO();

        try {
            dto.setId(studentTask.getId());
            dto.setTask(fromTaskToListDTO(studentTask.getTaskReference()));
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

    public TaskLinkDTO fromTaskToTaskLinkDTO(Task task) {
        TaskLinkDTO dto = new TaskLinkDTO();
        try {
            dto.setId(task.getId());
            dto.setName(task.getName());
        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("DTO: " + dto);
            throw new MappingException(e.getMessage());
        }
        return dto;
    }

    public List<TaskLinkDTO> fromTaskListToTaskLinkListDTO(List<Task> tasks) {
        return tasks.stream().map(this::fromTaskToTaskLinkDTO).toList();
    }

    public TaskLiteratureDTO fromLiteratureToTaskLiteratureDTO (Literature lit) {
        TaskLiteratureDTO dto = new TaskLiteratureDTO();
        try {
            dto.setId(lit.getId());
            dto.setName(lit.getName());
            dto.setType(lit.getType());
        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("DTO: " + dto);
            throw new MappingException(e.getMessage());
        }
        return dto;
    }

    public List<TaskLiteratureDTO> fromLiteratureListToTaskLiteratureListDTO(List<Literature> literature) {
        return literature.stream().map(this::fromLiteratureToTaskLiteratureDTO).toList();
    }

    public TaskInfoDTO fromTaskToInfoDTO(Task task) {
        TaskInfoDTO dto = new TaskInfoDTO();

        try {
            dto.setId(task.getId());
            dto.setName(task.getName());

            if(task.getParentTask() != null) {
                TaskLinkDTO parentTaskDTO = new TaskLinkDTO();
                parentTaskDTO.setId(task.getParentTask().getId());
                parentTaskDTO.setName(task.getParentTask().getName());

                dto.setParentTask(parentTaskDTO);
            }

            dto.setLevel(task.getLevel());
            dto.setCompletionTime(task.getCompletionTime());
            dto.setSkillsDescription(task.getSkillsDescription());
            dto.setTaskDescription(task.getTaskDescription());
            dto.setSubtasks(fromTaskListToTaskLinkListDTO(task.getSubtasks()));
            dto.setRecommendedLiterature(fromLiteratureListToTaskLiteratureListDTO(task.getRecommendedLiterature()));

            dto.setStudents(studentMapper.fromStudentListToAvatarListDTO(task.getActiveStudents()));

        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("DTO: " + dto);
            throw new MappingException(e.getMessage());
        }

        return dto;
    }

    public Task fromTaskSaveDTOToTask(TaskSaveDTO taskSaveDTO) {
        Task task = new Task();
        task.setId(taskSaveDTO.getId());
        task.setName(taskSaveDTO.getName());

        if (taskSaveDTO.getParentTaskID() != null) {
            Task parentTask = taskRepository.findById(taskSaveDTO.getParentTaskID()).orElseThrow();
            task.setParentTask(parentTask);
        }

        if (taskSaveDTO.getCourseID() != null) {
            Course course = courseRepository.findById(taskSaveDTO.getCourseID()).orElseThrow();
            task.setCourse(course);
        }

        task.setLevel(taskSaveDTO.getLevel());
        task.setCompletionTime(taskSaveDTO.getCompletionTime());
        task.setSkillsDescription(taskSaveDTO.getSkillsDescription());
        task.setTaskDescription(taskSaveDTO.getTaskDescription());

        List<Task> subtasks = new ArrayList<>();
        if (taskSaveDTO.getSubtasksIDs() != null) {
            for (Long subtaskId : taskSaveDTO.getSubtasksIDs()) {
                Task subtask = taskRepository.findById(subtaskId).orElseThrow();
                subtasks.add(subtask);
            }
        }
        task.setSubtasks(subtasks);

        List<Literature> recommendedLiterature = new ArrayList<>();
        taskSaveDTO.getLiteratureList().forEach(dto -> {
            Literature literature = literatureRepository.findById(dto.getId()).orElseThrow();
            recommendedLiterature.add(literature);
        });
        task.setRecommendedLiterature(recommendedLiterature);

        task.setStatus(taskSaveDTO.getStatus());

        return task;
    }

    public TaskCardDTO fromTaskToCardDTO(Task task) {
        TaskCardDTO dto = new TaskCardDTO();

        try {
            dto.setId(task.getId());
            dto.setName(task.getName());
            if (task.getParentTask() != null) {
                dto.setParentTaskId(task.getParentTask().getId());
                dto.setParentTaskName(task.getParentTask().getName());
            }

            if (task.getCourse() != null) {
                dto.setCourseId(task.getCourse().getId());
                dto.setCourseName(task.getCourse().getName());
            }

            dto.setLevel(task.getLevel());
            dto.setCompletionTime(task.getCompletionTime());
            dto.setSkillsDescription(task.getSkillsDescription());
            dto.setTaskDescription(task.getTaskDescription());

            Map<Long, String> subtaskMap = new HashMap<>();
            for (Task subtask : task.getSubtasks()) {
                subtaskMap.put(subtask.getId(), subtask.getName());
            }
            dto.setSubtasks(subtaskMap);

            Map<Long, String> literatureMap = new HashMap<>();
            for (Literature literature : task.getRecommendedLiterature()) {
                literatureMap.put(literature.getId(), literature.getName());
            }
            dto.setRecommendedLiterature(literatureMap);

            List<Long> activeStudentsIds = new ArrayList<>();
            if (task.getActiveStudents() != null && task.getActiveStudents().size() > 0) {
                for (Student activeStudent : task.getActiveStudents()) {
                    activeStudentsIds.add(activeStudent.getId());
                }
            }

            dto.setStatus(task.getStatus());


        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("DTO: " + dto);
            throw new MappingException(e.getMessage());
        }


        return dto;
    }


}
