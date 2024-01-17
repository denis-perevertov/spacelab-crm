package com.example.spacelab.integration.teamwork;

import com.example.spacelab.integration.TaskTrackingService;
import com.example.spacelab.integration.data.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamworkService implements TaskTrackingService {

    private final TeamworkClient client;

    @Override
    public UserResponse createTaskUser(UserCreateRequest request) {
        UserResponse response = client.createUser(request).getData();
        log.info("response in service: {}", response);
        return response;
    }

    @Override
    public TaskListResponse createTaskList(TaskListCreateRequest request) {
//        return client.createTaskList(request);
        return null;
    }

    @Override
    public Optional<TaskResponse> getTaskById(String taskId) {
//        return client.getTask(taskId).map(t -> new TaskResponse(
//                t.id(),
//                t.name(),
//                t.description(),
//                t.priority(),
//                t.progress(),
//                t.tagIds(),
//                t.status(),
//                t.startDate(),
//                t.parentTaskId(),
//                t.taskListId(),
//                t.assigneeIds()
//        ));
        return Optional.empty();
    }
}
