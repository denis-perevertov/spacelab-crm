package com.example.spacelab.integration.teamwork;

import com.example.spacelab.exception.TeamworkException;
import com.example.spacelab.integration.TaskTrackingService;
import com.example.spacelab.integration.data.*;
import com.example.spacelab.integration.teamwork.data.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public ProjectResponse createProject(ProjectRequest request) {
        TeamworkProjectRequest teamworkRequest = new TeamworkProjectRequest(
                new TeamworkProject(
                      request.id(),
                      request.name(),
                      request.description()
                )
        );
        ApiResponse<TeamworkProjectResponse> response = client.createProject(teamworkRequest);
        return Optional.ofNullable(response.getData())
                .map(res -> new ProjectResponse(
                        res.status(),
                        res.id()
                ))
                .orElseThrow(() -> new TeamworkException(response.getErrors()));
    }

    @Override
    public ProjectResponse updateProject(ProjectRequest request) {
        TeamworkProjectRequest teamworkRequest = new TeamworkProjectRequest(
                new TeamworkProject(
                        request.id(),
                        request.name(),
                        request.description()
                )
        );
        ApiResponse<TeamworkProjectResponse> response = client.updateProject(teamworkRequest);
        return Optional.ofNullable(response.getData())
                .map(res -> new ProjectResponse(
                        res.status(),
                        res.id()
                ))
                .orElseThrow(() -> new TeamworkException(response.getErrors()));
    }

    @Override
    public void deleteProject(String projectId) {
        Optional.ofNullable(client.deleteProject(projectId).getErrors())
                .ifPresent(errors -> {
                    throw new TeamworkException(errors);
                });
    }

    @Override
    public TaskListResponse createTaskList(TaskListRequest request) {
        TeamworkTaskListRequest teamworkRequest = new TeamworkTaskListRequest(
                request.id(),
                request.applyDefaultsToExistingTasks(),
                request.taskList()
        );
        ApiResponse<TeamworkTaskListCreateResponse> response = client.createTaskList(teamworkRequest);
        return Optional.ofNullable(response.getData())
                .map(res -> new TaskListResponse(
                        res.status(),
                        res.taskListId()
                ))
                .orElseThrow(() -> new TeamworkException(response.getErrors()));
    }

    @Override
    public TaskListResponse updateTaskList(TaskListRequest request) {
        TeamworkTaskListRequest teamworkRequest = new TeamworkTaskListRequest(
                request.id(),
                request.applyDefaultsToExistingTasks(),
                request.taskList()
        );
        ApiResponse<TeamworkTaskListCreateResponse> response = client.updateTaskList(teamworkRequest);
        return Optional.ofNullable(response.getData())
                .map(res -> new TaskListResponse(
                        res.status(),
                        res.taskListId()
                ))
                .orElseThrow(() -> new TeamworkException(response.getErrors()));
    }

    @Override
    public TaskResponse createTaskInTaskList(TaskCreateRequest request) {
        TeamworkTaskRequest teamworkRequest = new TeamworkTaskRequest(
                new TeamworkTaskRequest.Task(
                        request.name(),
                        request.description(),
                        request.priority(),
                        request.progress(),
                        request.tagIds(),
                        request.status(),
                        request.startDate(),
                        request.parentTaskId(),
                        request.taskListId(),
                        new TeamworkTaskAssignees(
                                null,
                                null,
                                request.userIds()
                        )
                )
        );
        ApiResponse<TeamworkTaskResponse> response = client.createNewTaskInList(teamworkRequest);
        return Optional.ofNullable(response.getData())
                .map(res -> new TaskResponse(
                        res.task().id(),
                        res.task().name(),
                        res.task().description(),
                        res.task().priority(),
                        res.task().progress(),
                        res.task().tagIds(),
                        res.task().status(),
                        res.task().startDate(),
                        res.task().parentTaskId(),
                        res.task().taskListId(),
                        res.task().assigneeIds()
                ))
                .orElseThrow(() -> new TeamworkException(response.getErrors()));
    }

    @Override
    public void deleteTaskListById(String taskListId) {
        Optional.ofNullable(client.deleteTaskList(taskListId).getErrors())
                .ifPresent(errors -> {
                    throw new TeamworkException(errors);
                });
    }

    @Override
    public List<TaskResponse> getAllTasksFromList(String taskListId) {
        ApiResponse<TeamworkTaskListResponse> response = client.getAllTasksFromTaskList(taskListId);
        return Optional.ofNullable(response.getData())
                .map(
                        list -> list.tasks().stream().map(task -> new TaskResponse (
                                task.id(),
                                task.name(),
                                task.description(),
                                task.priority(),
                                task.progress(),
                                task.tagIds(),
                                task.status(),
                                task.startDate(),
                                task.parentTaskId(),
                                task.taskListId(),
                                task.assigneeIds()
                        )).toList()
                )
                .orElseThrow(() -> new TeamworkException(response.getErrors()));
    }

    @Override
    public TaskResponse getTaskById(String taskId) {
        ApiResponse<TeamworkTaskResponse> response = client.getTask(taskId);
        return Optional.ofNullable(response.getData())
                .map(res -> new TaskResponse(
                        res.task().id(),
                        res.task().name(),
                        res.task().description(),
                        res.task().priority(),
                        res.task().progress(),
                        res.task().tagIds(),
                        res.task().status(),
                        res.task().startDate(),
                        res.task().parentTaskId(),
                        res.task().taskListId(),
                        res.task().assigneeIds()
                ))
                .orElseThrow(() -> new TeamworkException(response.getErrors()));
    }

    @Override
    public TaskResponse updateTaskById(String taskId, TaskUpdateRequest request) {
        TeamworkTaskRequest teamworkRequest = new TeamworkTaskRequest(
                new TeamworkTaskRequest.Task(
                        request.name(),
                        request.description(),
                        request.priority(),
                        request.progress(),
                        request.tagIds(),
                        request.status(),
                        request.startDate(),
                        request.parentTaskId(),
                        request.taskListId(),
                        new TeamworkTaskAssignees(
                                null,
                                null,
                                request.userIds()
                        )
                )
        );
        ApiResponse<TeamworkTaskResponse> response = client.updateTask(taskId, teamworkRequest);
        return Optional.ofNullable(response.getData())
                .map(res -> new TaskResponse(
                        res.task().id(),
                        res.task().name(),
                        res.task().description(),
                        res.task().priority(),
                        res.task().progress(),
                        res.task().tagIds(),
                        res.task().status(),
                        res.task().startDate(),
                        res.task().parentTaskId(),
                        res.task().taskListId(),
                        res.task().assigneeIds()
                ))
                .orElseThrow(() -> new TeamworkException(response.getErrors()));
    }

    @Override
    public void deleteTaskById(String taskId) {
        Optional.ofNullable(client.deleteTask(taskId).getErrors())
                .ifPresent(errors -> {
                    throw new TeamworkException(errors);
                });
    }

    @Override
    public void completeTask(String taskId) {
        Optional.ofNullable(client.completeTask(taskId).getErrors())
                .ifPresent(errors -> {
                    throw new TeamworkException(errors);
                });
    }

    @Override
    public void uncompleteTask(String taskId) {
        Optional.ofNullable(client.uncompleteTask(taskId).getErrors())
                .ifPresent(errors -> {
                    throw new TeamworkException(errors);
                });
    }

    @Override
    public List<TaskResponse> getTaskSubtasks(String taskId) {
        ApiResponse<TeamworkTaskListResponse> response = client.getTaskSubtasks(taskId);
        return Optional.ofNullable(response.getData())
                .map(
                        list -> list.tasks().stream().map(task -> new TaskResponse (
                                task.id(),
                                task.name(),
                                task.description(),
                                task.priority(),
                                task.progress(),
                                task.tagIds(),
                                task.status(),
                                task.startDate(),
                                task.parentTaskId(),
                                task.taskListId(),
                                task.assigneeIds()
                        )).toList()
                )
                .orElseThrow(() -> new TeamworkException(response.getErrors()));
    }

    @Override
    public TaskResponse createTaskSubtask(TaskCreateRequest request) {
        TeamworkTaskRequest teamworkRequest = new TeamworkTaskRequest(
                new TeamworkTaskRequest.Task(
                        request.name(),
                        request.description(),
                        request.priority(),
                        request.progress(),
                        request.tagIds(),
                        request.status(),
                        request.startDate(),
                        request.parentTaskId(),
                        request.taskListId(),
                        new TeamworkTaskAssignees(
                                null,
                                null,
                                request.userIds()
                        )
                )
        );
        ApiResponse<TeamworkTaskResponse> response = client.createSubtask(teamworkRequest);
        return Optional.ofNullable(response.getData())
                .map(res -> new TaskResponse(
                        res.task().id(),
                        res.task().name(),
                        res.task().description(),
                        res.task().priority(),
                        res.task().progress(),
                        res.task().tagIds(),
                        res.task().status(),
                        res.task().startDate(),
                        res.task().parentTaskId(),
                        res.task().taskListId(),
                        res.task().assigneeIds()
                ))
                .orElseThrow(() -> new TeamworkException(response.getErrors()));
    }

    @Override
    public List<TimeEntryResponse> getAllTimeEntriesForTask(String taskId) {
        ApiResponse<TeamworkTimeEntryListResponse> response = client.getTaskTimeEntries(taskId);
        return Optional.ofNullable(response.getData())
                .map(
                        list -> list.timelogs().stream().map(timeEntry -> new TimeEntryResponse (
                                timeEntry.id(),
                                timeEntry.minutes(),
                                timeEntry.beginTime(),
                                timeEntry.userId(),
                                timeEntry.taskId(),
                                timeEntry.projectId()
                        )).toList()
                )
                .orElseThrow(() -> new TeamworkException(response.getErrors()));
    }

    @Override
    public TimeEntryResponse getTimeEntry(String timelogId) {
        ApiResponse<TeamworkTimeEntryResponse> response = client.getTaskSingleTimeEntry(timelogId);
        return Optional.ofNullable(response.getData())
                .map(TeamworkTimeEntryResponse::timelog)
                .map(timeEntry -> new TimeEntryResponse(
                        timeEntry.id(),
                        timeEntry.minutes(),
                        timeEntry.beginTime(),
                        timeEntry.userId(),
                        timeEntry.taskId(),
                        timeEntry.projectId()
                ))
                .orElseThrow(() -> new TeamworkException(response.getErrors()));
    }

    @Override
    public TimeEntryResponse createTimeEntryForTask(TimeEntryRequest request) {
        TeamworkTimeEntryCreateRequest teamworkRequest = new TeamworkTimeEntryCreateRequest(
                request.timelog(),
                request.tags(),
                request.timelogOptions()
        );
        ApiResponse<TeamworkTimeEntryResponse> response = client.createTaskTimeEntry(teamworkRequest);
        return Optional.ofNullable(response.getData())
                .map(TeamworkTimeEntryResponse::timelog)
                .map(timeEntry -> new TimeEntryResponse(
                        timeEntry.id(),
                        timeEntry.minutes(),
                        timeEntry.beginTime(),
                        timeEntry.userId(),
                        timeEntry.taskId(),
                        timeEntry.projectId()
                ))
                .orElseThrow(() -> new TeamworkException(response.getErrors()));
    }

    @Override
    public TimeEntryResponse updateTimeEntryForTask(TimeEntryRequest request) {
        TeamworkTimeEntryCreateRequest teamworkRequest = new TeamworkTimeEntryCreateRequest(
                request.timelog(),
                request.tags(),
                request.timelogOptions()
        );
        ApiResponse<TeamworkTimeEntryResponse> response = client.updateTaskTimeEntry(request.timelog().id(), teamworkRequest);
        return Optional.ofNullable(response.getData())
                .map(TeamworkTimeEntryResponse::timelog)
                .map(timeEntry -> new TimeEntryResponse(
                        timeEntry.id(),
                        timeEntry.minutes(),
                        timeEntry.beginTime(),
                        timeEntry.userId(),
                        timeEntry.taskId(),
                        timeEntry.projectId()
                ))
                .orElseThrow(() -> new TeamworkException(response.getErrors()));
    }

    @Override
    public void deleteTimeEntry(String timelogId) {
        Optional.ofNullable(client.deleteTaskTimeEntry(timelogId).getErrors())
                .ifPresent(errors -> {
                    throw new TeamworkException(errors);
                });
    }

    @Override
    public TimeTotalResponse getTotalTimeOnTaskList(String taskListId) {
        ApiResponse<TeamworkTimeTotalResponse> response = client.getTaskListTimeTotal(taskListId);
        return Optional.ofNullable(response.getData())
                .map(timeTotal -> new TimeTotalResponse(
                        new TimeTotal(
                                timeTotal.taskTimeTotal().minutes(),
                                timeTotal.taskTimeTotal().estimatedMinutes()
                        ),
                        new TimeTotal(
                                timeTotal.subtaskTimeTotal().minutes(),
                                timeTotal.subtaskTimeTotal().estimatedMinutes()
                        )
                ))
                .orElseThrow(() -> new TeamworkException(response.getErrors()));
    }

    @Override
    public TimeTotalResponse getTotalTimeOnTask(String taskId) {
        ApiResponse<TeamworkTimeTotalResponse> response = client.getTaskTimeTotal(taskId);
        return Optional.ofNullable(response.getData())
                .map(timeTotal -> new TimeTotalResponse(
                        new TimeTotal(
                                timeTotal.taskTimeTotal().minutes(),
                                timeTotal.taskTimeTotal().estimatedMinutes()
                        ),
                        new TimeTotal(
                                timeTotal.subtaskTimeTotal().minutes(),
                                timeTotal.subtaskTimeTotal().estimatedMinutes()
                        )
                ))
                .orElseThrow(() -> new TeamworkException(response.getErrors()));
    }

    @Override
    public TagResponse getTagByName(String tagName) {
        ApiResponse<TeamworkTagResponse> response = client.getTagBySearchTerm(tagName);
        return Optional.ofNullable(response.getData())
                .map(TeamworkTagResponse::tags)
                .flatMap(tags -> tags.stream().findFirst())
                .map(tag -> new TagResponse(
                        tag.id(),
                        tag.projectId(),
                        tag.name(),
                        tag.color()
                ))
                .orElseThrow(() -> new TeamworkException(response.getErrors()));
    }

    @Override
    public TagResponse getTagById(String tagId) {
        ApiResponse<TeamworkTagResponse> response = client.getTagById(tagId);
        return Optional.ofNullable(response.getData())
                .map(TeamworkTagResponse::tags)
                .flatMap(tags -> tags.stream().findFirst())
                .map(tag -> new TagResponse(
                        tag.id(),
                        tag.projectId(),
                        tag.name(),
                        tag.color()
                ))
                .orElseThrow(() -> new TeamworkException(response.getErrors()));
    }

    @Override
    public String getRecommendedTagName() {
        return "recommended";
    }


}
