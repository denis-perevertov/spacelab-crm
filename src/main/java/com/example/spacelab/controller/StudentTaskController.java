package com.example.spacelab.controller;

import com.example.spacelab.api.StudentTaskAPI;
import com.example.spacelab.dto.student.StudentTaskDTO;
import com.example.spacelab.mapper.StudentMapper;
import com.example.spacelab.mapper.TaskMapper;
import com.example.spacelab.model.student.StudentTask;
import com.example.spacelab.service.StudentService;
import com.example.spacelab.service.TaskService;
import com.example.spacelab.util.AuthUtil;
import com.example.spacelab.util.FilterForm;
import com.example.spacelab.validator.StudentValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/students/tasks")
public class StudentTaskController implements StudentTaskAPI {

    private final StudentService studentService;
    private final TaskService taskService;
    private final StudentMapper studentMapper;
    private final StudentValidator studentValidator;
    private final TaskMapper taskMapper;

    private final AuthUtil authUtil;

    @GetMapping
    public ResponseEntity<Object> getTasksOfSingleStudent(FilterForm filters,
                                                          @RequestParam(required = false, defaultValue = "0") int page,
                                                          @RequestParam(required = false, defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<StudentTaskDTO> taskPage = taskService
                .getStudentTasks(taskService.buildSpec(filters.trim()), pageable)
                .map(taskMapper::fromStudentTaskToDTO);

        return ResponseEntity.ok().body(taskPage);
    }

    @GetMapping("/{taskID}")
    public ResponseEntity<Object> getSingleTaskOfSingleStudent(@PathVariable Long taskID) {

        // todo check course access
        return ResponseEntity.ok().body(taskMapper.fromStudentTaskToDTO(taskService.getStudentTask(taskID)));
    }

    @GetMapping("/{taskID}/info")
    public ResponseEntity<?> getStudentTaskInfo(@PathVariable Long taskID) {

        StudentTask st = taskService.getStudentTask(taskID);
        return ResponseEntity.ok(taskMapper.studentTaskToCardDTO(st));
    }

    @GetMapping("/{taskID}/points")
    public ResponseEntity<?> getTaskProgressPoints(@PathVariable Long taskID) {
        return ResponseEntity.ok(taskService.getStudentTaskProgressPoints(taskID));
    }

    @PostMapping("/complete")
    public ResponseEntity<Object> completeStudentTask(@RequestBody Long taskID) {
        log.info("completing task {}", taskID);
        taskService.completeStudentTask(taskID);
        return ResponseEntity.ok("Student Task w/ ID: "+taskID+" completed");
    }

    @PostMapping("/unlock")
    public ResponseEntity<?> unlockStudentTask(@RequestBody Long taskID) {
        log.info("unlocking task {}", taskID);
        taskService.unlockStudentTask(taskID);
        return ResponseEntity.ok("Student Task w/ ID: "+taskID+" unlocked");
    }

    @PostMapping("/lock")
    public ResponseEntity<?> lockStudentTask(@RequestBody Long taskID) {
        log.info("locking task {}", taskID);
        taskService.lockStudentTask(taskID);
        return ResponseEntity.ok("Student Task w/ ID: "+taskID+" locked");
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetStudentTask(@RequestBody Long taskID) {
        log.info("resetting task {}", taskID);
        taskService.resetStudentTask(taskID);
        return ResponseEntity.ok("Student Task w/ ID: "+taskID+" reset");
    }


}
