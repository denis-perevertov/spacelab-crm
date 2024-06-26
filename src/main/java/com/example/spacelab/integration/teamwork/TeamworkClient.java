package com.example.spacelab.integration.teamwork;

import com.example.spacelab.config.TeamworkProperties;
import com.example.spacelab.integration.data.ApiResponse;
import com.example.spacelab.integration.data.ErrorResponse;
import com.example.spacelab.integration.teamwork.data.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class TeamworkClient {

    private final TeamworkProperties properties;

    private final WebClient.Builder clientBuilder;

    public ApiResponse<TeamworkTaskResponse> getTask(String taskId) {

        log.info("client request: {}", taskId);

        WebClient client = clientBuilder.build();

        ApiResponse<TeamworkTaskResponse> response = client.get()
                .uri(properties.getBaseUrl() + "/projects/api/" + properties.getApiVersion() + "/tasks/" + taskId + ".json")
                .headers(headers -> headers.setBasicAuth(properties.getToken(), ""))
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(TeamworkTaskResponse.class)
                                .map(data -> new ApiResponse<>(
                                        data,
                                        null,
                                        res.statusCode().toString()
                                ));
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<TeamworkTaskResponse>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .block();

        log.info("client response: {}", response);

        return response;
    }

    public ApiResponse<TeamworkUserResponse> createUser(TeamworkUserCreateRequest request) {

        log.info("client request: {}", request);

        WebClient client = clientBuilder.build();

        ApiResponse<TeamworkUserResponse> response = client.post()
                .uri(properties.getBaseUrl() + "/people.json")
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(TeamworkUserResponse.class)
                                .map(data -> new ApiResponse<>(
                                        data,
                                        null,
                                        res.statusCode().toString()
                                ));
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<TeamworkUserResponse>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .block();

        log.info("client response: {}", response);

        return response;
    }

    public ApiResponse<TeamworkTaskListCreateResponse> createTaskList(String projectId, TeamworkTaskListRequest request) {

        log.info("client request: {}", request);

        WebClient client = clientBuilder.build();

        ApiResponse<TeamworkTaskListCreateResponse> response = client.post()
                .uri(properties.getBaseUrl() +
                        "/projects/" + projectId +
                        "/tasklists.json")
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(TeamworkTaskListCreateResponse.class)
                                .map(data -> new ApiResponse<>(
                                        data,
                                        null,
                                        res.statusCode().toString()
                                ));
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<TeamworkTaskListCreateResponse>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .block();

        log.info("client response: {}", response);

        return response;
    }

    public ApiResponse<TeamworkTaskListCreateResponse> updateTaskList(TeamworkTaskListRequest request) {

        log.info("client request: {}", request);

        WebClient client = clientBuilder
                .baseUrl(properties.getBaseUrl())
                .build();

        ApiResponse<TeamworkTaskListCreateResponse> response = client.put()
                .uri(builder -> builder
                        .path("/tasklists" + request.id())
                        .build())
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(TeamworkTaskListCreateResponse.class)
                                .map(data -> new ApiResponse<>(
                                        data,
                                        null,
                                        res.statusCode().toString()
                                ));
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<TeamworkTaskListCreateResponse>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .block();

        log.info("client response: {}", response);

        return response;
    }

    public ApiResponse<Void> deleteTaskList(String taskListId) {

        log.info("client request: {}", taskListId);

        WebClient client = clientBuilder.build();

        ApiResponse<Void> response = client.delete()
                .uri(properties.getBaseUrl() + "/tasklists/" + taskListId)
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                })
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return Mono.empty();
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<Void>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .defaultIfEmpty(new ApiResponse<>(null, null, HttpStatus.OK.toString()))
                .block();

        log.info("client response: {}", response);

        return response;
    }

    public ApiResponse<TeamworkTaskResponse> createNewTaskInList(TeamworkTaskRequest request) {
        log.info("client request: {}", request);

        WebClient client = clientBuilder.build();

        ApiResponse<TeamworkTaskResponse> response = client.post()
                .uri(properties.getBaseUrl() +
                        "/projects/api/" + properties.getApiVersion() +
                        "/tasklists/" + request.task().taskListId() + "/tasks.json")
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(TeamworkTaskResponse.class)
                                .map(data -> new ApiResponse<>(
                                        data,
                                        null,
                                        res.statusCode().toString()
                                ));
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<TeamworkTaskResponse>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .block();

        log.info("client response: {}", response);

        return response;
    }

    public ApiResponse<TeamworkTaskResponse> updateTask(String taskId, TeamworkTaskRequest request) {

        log.info("client request: {}", request);

        WebClient client = clientBuilder.build();

        ApiResponse<TeamworkTaskResponse> response = client.patch()
                .uri(properties.getBaseUrl() +
                        "/projects/api/" + properties.getApiVersion() +
                        "/tasks/" + taskId + ".json")
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(TeamworkTaskResponse.class)
                                .map(data -> new ApiResponse<>(
                                        data,
                                        null,
                                        res.statusCode().toString()
                                ));
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<TeamworkTaskResponse>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .block();

        log.info("client response: {}", response);

        return response;
    }

    public ApiResponse<Void> deleteTask(String taskId) {

        log.info("client request: {}", taskId);

        WebClient client = clientBuilder.build();

        ApiResponse<Void> response = client.delete()
                .uri(properties.getBaseUrl() +
                        "/projects/api/" + properties.getApiVersion() +
                        "/tasks/" + taskId + ".json")
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                })
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return Mono.empty();
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<Void>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .defaultIfEmpty(new ApiResponse<>(null, null, HttpStatus.OK.toString()))
                .block();

        log.info("client response: {}", response);

        return response;
    }

    public ApiResponse<TeamworkTaskListResponse> getAllTasksFromTaskList(String taskListId) {

        log.info("client request: {}", taskListId);

        WebClient client = clientBuilder
                .baseUrl(properties.getBaseUrl())
                .build();

        ApiResponse<TeamworkTaskListResponse> response = client.get()
                .uri(builder -> builder
                        .path("/projects/api/v3/tasklists/" + taskListId + "/tasks.json")
                        .queryParam("includeCompletedTasks", true)
                        .queryParam("includeRelatedTasks", true)
                        .build())
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                })
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(TeamworkTaskListResponse.class)
                                .map(data -> new ApiResponse<>(
                                        data,
                                        null,
                                        res.statusCode().toString()
                                ));
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<TeamworkTaskListResponse>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .block();

        log.info("client response: {}", response);

        if(response != null && response.getData() != null && response.getData().tasks().isEmpty()) {
            log.info("tasks not found, trying api v1 url");

            response = client.get()
                    .uri(builder -> builder
                            .path("/tasklists/" + taskListId + "/tasks.json")
                            .queryParam("includeCompletedTasks", true)
                            .queryParam("includeRelatedTasks", true)
                            .build())
                    .headers(headers -> {
                        headers.setBasicAuth(properties.getToken(), "");
                    })
                    .exchangeToMono(res -> {
                        if(res.statusCode().is2xxSuccessful()) {
                            return res.bodyToMono(TeamworkTaskListV1Response.class)
                                    .map(data -> new ApiResponse<>(
                                            new TeamworkTaskListResponse(
                                                   data.tasks().stream().map(task -> new TeamworkTask(
                                                         task.id(),
                                                         task.content(),
                                                         "",
                                                         "",
                                                         0,
                                                           (
                                                               task.tags() == null || task.tags().length == 0
                                                               ? new Long[]{}
                                                               : Arrays.stream(task.tags()).map(TeamworkTaskListV1Response.TeamworkTaskV1.Tag::id).toArray(Long[]::new)
                                                           ),
                                                           task.status(),
                                                           ZonedDateTime.now(),
                                                           (
                                                                   task.parentTaskId() == null || task.parentTaskId().isEmpty()
                                                                   ? 0L
                                                                   : Long.parseLong(task.parentTaskId())
                                                           ),
                                                           Long.valueOf(taskListId),
                                                           new Integer[]{},
                                                           new Long[]{}
                                                   )).toList(),
                                                    new Object(),
                                                    new Object()
                                            ),
                                            null,
                                            res.statusCode().toString()
                                    ));
                        }
                        else {
                            return res.bodyToMono(ErrorResponse.class)
                                    .map(errors -> new ApiResponse<TeamworkTaskListResponse>(
                                            null,
                                            errors.errors(),
                                            res.statusCode().toString()
                                    ));
                        }
                    })
                    .block();



        }

        return response;

    }

    public ApiResponse<TeamworkTagListResponse> getTagBySearchTerm(String searchTerm) {

        log.info("client request: {}", searchTerm);

        WebClient client = clientBuilder.build();
        String uri = properties.getBaseUrl() +
                "/projects/api/" + properties.getApiVersion() +
                "/tags.json?searchTerm="+searchTerm;

        ApiResponse<TeamworkTagListResponse> response = client.get()
                .uri(uri)
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                })
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(TeamworkTagListResponse.class)
                                .map(data -> new ApiResponse<>(
                                        data,
                                        null,
                                        res.statusCode().toString()
                                ));
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<TeamworkTagListResponse>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .block();

        log.info("client response: {}", response);

        return response;
    }

    public ApiResponse<TeamworkTagResponse> getTagById(String tagId) {
        log.info("client request: {}", tagId);

        WebClient client = clientBuilder.build();
        String uri = properties.getBaseUrl() +
                "/tags/" + tagId + ".json";

        ApiResponse<TeamworkTagResponse> response = client.get()
                .uri(uri)
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                })
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(TeamworkTagResponse.class)
                                .map(data -> new ApiResponse<>(
                                        data,
                                        null,
                                        res.statusCode().toString()
                                ));
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<TeamworkTagResponse>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .block();

        log.info("client response: {}", response);

        return response;
    }

    public ApiResponse<TeamworkTaskListResponse> getTaskSubtasks(String taskId) {

        log.info("client request: {}", taskId);

        WebClient client = clientBuilder.build();

        ApiResponse<TeamworkTaskListResponse> response = client.get()
                .uri(properties.getBaseUrl() +
                        "/projects/api/" + properties.getApiVersion() +
                        "/tasks/" + taskId + "/subtasks.json")
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                })
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(TeamworkTaskListResponse.class)
                                .map(data -> new ApiResponse<>(
                                        data,
                                        null,
                                        res.statusCode().toString()
                                ));
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<TeamworkTaskListResponse>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .block();

        log.info("client response: {}", response);

        return response;

    }

    public ApiResponse<TeamworkTaskResponse> createSubtask(TeamworkTaskRequest request) {
        log.info("client request: {}", request);

        WebClient client = clientBuilder.build();

        ApiResponse<TeamworkTaskResponse> response = client.post()
                .uri(properties.getBaseUrl() +
                        "/projects/api/" + properties.getApiVersion() +
                        "/tasks/" + request.task().parentTaskId() + "/subtasks.json")
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(TeamworkTaskResponse.class)
                                .map(data -> new ApiResponse<>(
                                        data,
                                        null,
                                        res.statusCode().toString()
                                ));
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<TeamworkTaskResponse>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .block();

        log.info("client response: {}", response);

        return response;
    }

    public ApiResponse<TeamworkTimeEntryListResponse> getTaskTimeEntries(String taskId) {

        log.info("client request: {}", taskId);

        WebClient client = clientBuilder.build();

        ApiResponse<TeamworkTimeEntryListResponse> response = client.get()
                .uri(properties.getBaseUrl() +
                        "/projects/api/" + properties.getApiVersion() +
                        "/tasks/" + taskId + "/time.json")
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                })
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(TeamworkTimeEntryListResponse.class)
                                .map(data -> new ApiResponse<>(
                                        data,
                                        null,
                                        res.statusCode().toString()
                                ));
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<TeamworkTimeEntryListResponse>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .block();

        log.info("client response: {}", response);

        return response;

    }

    public ApiResponse<TeamworkTimeEntryResponse> getTaskSingleTimeEntry(String timeEntryId) {

        log.info("client request: {}", timeEntryId);

        WebClient client = clientBuilder.build();

        ApiResponse<TeamworkTimeEntryResponse> response = client.get()
                .uri(properties.getBaseUrl() +
                        "/projects/api/" + properties.getApiVersion() +
                        "/time/" + timeEntryId + ".json")
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                })
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(TeamworkTimeEntryResponse.class)
                                .map(data -> new ApiResponse<>(
                                        data,
                                        null,
                                        res.statusCode().toString()
                                ));
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<TeamworkTimeEntryResponse>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .block();

        log.info("client response: {}", response);

        return response;

    }

    public ApiResponse<TeamworkTimeEntryResponse> createTaskTimeEntry(TeamworkTimeEntryCreateRequest request) {

        log.info("client request: {}", request);

        WebClient client = clientBuilder.build();

        ApiResponse<TeamworkTimeEntryResponse> response = client.post()
                .uri(properties.getBaseUrl() +
                        "/projects/api/" + properties.getApiVersion() +
                        "/tasks/" + request.timelog().taskId() + "/time.json")
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(TeamworkTimeEntryResponse.class)
                                .map(data -> new ApiResponse<>(
                                        data,
                                        null,
                                        res.statusCode().toString()
                                ));
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<TeamworkTimeEntryResponse>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .block();

        log.info("client response: {}", response);

        return response;

    }

    public ApiResponse<TeamworkTimeEntryResponse> updateTaskTimeEntry(String timelogId, TeamworkTimeEntryCreateRequest request) {

        log.info("client request: {}", request);

        WebClient client = clientBuilder.build();

        ApiResponse<TeamworkTimeEntryResponse> response = client.patch()
                .uri(properties.getBaseUrl() +
                        "/projects/api/" + properties.getApiVersion() +
                        "/time/" + timelogId + ".json")
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(TeamworkTimeEntryResponse.class)
                                .map(data -> new ApiResponse<>(
                                        data,
                                        null,
                                        res.statusCode().toString()
                                ));
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<TeamworkTimeEntryResponse>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .block();

        log.info("client response: {}", response);

        return response;

    }

    public ApiResponse<Void> deleteTaskTimeEntry(String timelogId) {

        log.info("client request: {}", timelogId);

        WebClient client = clientBuilder.build();

        ApiResponse<Void> response = client.delete()
                .uri(properties.getBaseUrl() +
                        "/projects/api/" + properties.getApiVersion() +
                        "/time/" + timelogId + ".json")
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                })
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return Mono.empty();
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<Void>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .defaultIfEmpty(new ApiResponse<>(null, null, HttpStatus.OK.toString()))
                .block();

        log.info("client response: {}", response);

        return response;
    }

    public ApiResponse<TeamworkTimeTotalResponse> getTaskTimeTotal(String taskId) {

        log.info("client request: {}", taskId);

        WebClient client = clientBuilder.build();

        ApiResponse<TeamworkTimeTotalResponse> response = client.get()
                .uri(properties.getBaseUrl() +
                        "/projects/api/" + properties.getApiVersion() +
                        "/tasks/" + taskId + "/time/total.json")
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                })
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(TeamworkTimeTotalResponse.class)
                                .map(data -> new ApiResponse<>(
                                        data,
                                        null,
                                        res.statusCode().toString()
                                ));
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<TeamworkTimeTotalResponse>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .block();

        log.info("client response: {}", response);

        return response;

    }

    public ApiResponse<TeamworkTimeTotalResponse> getTaskListTimeTotal(String taskListId) {

        log.info("client request: {}", taskListId);

        WebClient client = clientBuilder.build();

        ApiResponse<TeamworkTimeTotalResponse> response = client.get()
                .uri(properties.getBaseUrl() +
                        "/projects/api/" + properties.getApiVersion() +
                        "/tasklists/" + taskListId + "/time/total.json")
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                })
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(TeamworkTimeTotalResponse.class)
                                .map(data -> new ApiResponse<>(
                                        data,
                                        null,
                                        res.statusCode().toString()
                                ));
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<TeamworkTimeTotalResponse>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .block();

        log.info("client response: {}", response);

        return response;

    }

    public ApiResponse<TeamworkUserAddResponse> addUsersToProject(String projectId, TeamworkUserAddRequest request) {

        log.info("client request: {}", request);

        WebClient client = clientBuilder.build();

        ApiResponse<TeamworkUserAddResponse> response = client.put()
                .uri(properties.getBaseUrl() +
                        "/projects/api/" + properties.getApiVersion() +
                        "/projects/" + projectId + "/people.json")
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .bodyValue(request)
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(TeamworkUserAddResponse.class)
                                .map(data -> new ApiResponse<>(
                                        data,
                                        null,
                                        res.statusCode().toString()
                                ));
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<TeamworkUserAddResponse>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .block();

        log.info("client response: {}", response);

        return response;
    }

    public ApiResponse<TeamworkUserRemoveResponse> removeUsersFromProject(String projectId, TeamworkUserRemoveRequest request) {

        log.info("client request: {}", request);

        WebClient client = clientBuilder.build();

        ApiResponse<TeamworkUserRemoveResponse> response = client.put()
                .uri(properties.getBaseUrl() +
                        "/projects/" + projectId + "/people.json")
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .bodyValue(request)
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(TeamworkUserRemoveResponse.class)
                                .map(data -> new ApiResponse<>(
                                        data,
                                        null,
                                        res.statusCode().toString()
                                ));
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<TeamworkUserRemoveResponse>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .block();

        log.info("client response: {}", response);

        return response;

    }

    public ApiResponse<TeamworkProjectResponse> createProject(TeamworkProjectRequest request) {

        log.info("client request: {}", request);

        WebClient client = clientBuilder.build();

        ApiResponse<TeamworkProjectResponse> response = client.post()
                .uri(properties.getBaseUrl() +
                        "/projects.json")
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .bodyValue(request)
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(TeamworkProjectResponse.class)
                                .map(data -> new ApiResponse<>(
                                        data,
                                        null,
                                        res.statusCode().toString()
                                ));
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<TeamworkProjectResponse>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .block();

        log.info("client response: {}", response);

        return response;

    }

    public ApiResponse<TeamworkProjectResponse> updateProject(TeamworkProjectRequest request) {

        log.info("client request: {}", request);

        WebClient client = clientBuilder
                .baseUrl(properties.getBaseUrl())
                .build();

        ApiResponse<TeamworkProjectResponse> response = client.put()
                .uri(builder -> builder
                        .path("/projects/" + request.project().id() + ".json")
                        .build())
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .bodyValue(request)
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(TeamworkProjectResponse.class)
                                .map(data -> new ApiResponse<>(
                                        data,
                                        null,
                                        res.statusCode().toString()
                                ));
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<TeamworkProjectResponse>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .block();

        log.info("client response: {}", response);

        return response;

    }

    public ApiResponse<Void> deleteProject(String projectId) {

        log.info("client request: {}", projectId);

        WebClient client = clientBuilder.build();

        ApiResponse<Void> response = client.delete()
                .uri(properties.getBaseUrl() +
                        "/projects/" + projectId + ".json")
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                })
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return Mono.empty();
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<Void>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .defaultIfEmpty(new ApiResponse<>(null, null, HttpStatus.OK.toString()))
                .block();

        log.info("client response: {}", response);

        return response;
    }

    public ApiResponse<TeamworkTimeEntryResponse> createProjectTimeEntry(TeamworkTimeEntryCreateRequest request) {

        log.info("client request: {}", request);

        WebClient client = clientBuilder.build();

        ApiResponse<TeamworkTimeEntryResponse> response = client.post()
                .uri(properties.getBaseUrl() +
                        "/projects/api/" + properties.getApiVersion() +
                        "/projects/" + request.timelog().projectId() + "/time.json")
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(TeamworkTimeEntryResponse.class)
                                .map(data -> new ApiResponse<>(
                                        data,
                                        null,
                                        res.statusCode().toString()
                                ));
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<TeamworkTimeEntryResponse>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .block();

        log.info("client response: {}", response);

        return response;

    }

    public ApiResponse<Void> completeTask(String taskId) {

        log.info("client request: {}", taskId);

        WebClient client = clientBuilder.build();

        ApiResponse<Void> response = client.put()
                .uri(properties.getBaseUrl() +
                        "/tasks/" + taskId + "/complete.json")
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                })
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return Mono.empty();
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<Void>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .defaultIfEmpty(new ApiResponse<>(null, null, HttpStatus.ACCEPTED.toString()))
                .block();

        log.info("client response: {}", response);

        return response;

    }

    public ApiResponse<Void> uncompleteTask(String taskId) {

        log.info("client request: {}", taskId);

        WebClient client = clientBuilder.build();

        ApiResponse<Void> response = client.put()
                .uri(properties.getBaseUrl() +
                        "/tasks/" + taskId + "/uncomplete.json")
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                })
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return Mono.empty();
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<Void>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .defaultIfEmpty(new ApiResponse<>(null, null, HttpStatus.ACCEPTED.toString()))
                .block();

        log.info("client response: {}", response);

        return response;

    }

    public ApiResponse<TeamworkTimeEntryListResponse> getUserTimeEntries(TeamworkTimeEntryUserRequest request) {
        log.info("client request: {}", request);

        WebClient client = clientBuilder
                .baseUrl(properties.getBaseUrl())
                .build();

        ApiResponse<TeamworkTimeEntryListResponse> response = client.get()
                .uri(builder -> builder
                        .path("/projects/" + request.projectId() + "/time_entries.json")
                        .queryParam("fromdate", request.fromdate())
                        .queryParam("fromtime", request.fromtime())
                        .queryParam("todate", request.todate())
                        .queryParam("totime", request.totime())
                        .queryParam("page", request.page())
                        .queryParam("pageSize", request.pageSize())
                        .build())
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                })
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(TeamworkTimeEntryListResponse.class)
                                .map(data -> new ApiResponse<>(
                                        data,
                                        null,
                                        res.statusCode().toString()
                                ));
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<TeamworkTimeEntryListResponse>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .block();

        log.info("client response: {}", response);

        return response;
    }


    // fixme change endpoints from api v1 to api v3
    public ApiResponse<TeamworkAccountTimeTotalResponse> getAccountTimeTotal() {
        WebClient client = clientBuilder
                .baseUrl(properties.getBaseUrl())
                .build();

        ApiResponse<TeamworkAccountTimeTotalResponse> response = client.get()
                .uri(builder -> builder
                        .path("/time/total.json")
                        .build())
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                })
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(TeamworkAccountTimeTotalResponse.class)
                                .map(data -> new ApiResponse<>(
                                        data,
                                        null,
                                        res.statusCode().toString()
                                ));
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<TeamworkAccountTimeTotalResponse>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .block();

        log.info("client response: {}", response);

        return response;
    }

    // fixme change endpoints from api v1 to api v3
    public ApiResponse<TeamworkAccountTimeTotalResponse> getAccountTimeThisMonth() {

        String thisMonthBeginString = LocalDateTime.now().withDayOfMonth(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String nextMonthBeginString = LocalDateTime.now().plusMonths(1).withDayOfMonth(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        WebClient client = clientBuilder
                .baseUrl(properties.getBaseUrl())
                .build();

        ApiResponse<TeamworkAccountTimeTotalResponse> response = client.get()
                .uri(builder -> builder
                        .path("/time/total.json")
                        .queryParam("fromDate", thisMonthBeginString)
                        .queryParam("toDate", nextMonthBeginString)
                        .build())
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                })
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(TeamworkAccountTimeTotalResponse.class)
                                .map(data -> new ApiResponse<>(
                                        data,
                                        null,
                                        res.statusCode().toString()
                                ));
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<TeamworkAccountTimeTotalResponse>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .block();

        log.info("client response: {}", response);

        return response;
    }

    public ApiResponse<TeamworkTimeTotalResponse> getUserTimeTotal(String userId) {

        WebClient client = clientBuilder
                .baseUrl(properties.getBaseUrl())
                .build();

        ApiResponse<TeamworkTimeTotalResponse> response = client.get()
                .uri(builder -> builder
                        .path("/projects/api/v3/time/total.json")
                        .queryParam("assignedToUserIds", userId)
                        .build())
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                })
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(TeamworkTimeTotalResponse.class)
                                .map(data -> new ApiResponse<>(
                                        data,
                                        null,
                                        res.statusCode().toString()
                                ));
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<TeamworkTimeTotalResponse>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .block();

        log.info("client response: {}", response);

        return response;
    }

    public ApiResponse<TeamworkTimeTotalResponse> getUserTimeTotalThisMonth(String userId) {

        String thisMonthBeginString = LocalDateTime.now().withDayOfMonth(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String nextMonthBeginString = LocalDateTime.now().plusMonths(1).withDayOfMonth(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        WebClient client = clientBuilder
                .baseUrl(properties.getBaseUrl())
                .build();

        ApiResponse<TeamworkTimeTotalResponse> response = client.get()
                .uri(builder -> builder
                        .path("/projects/api/v3/time/total.json")
                        .queryParam("assignedToUserIds", userId)
                        .queryParam("startDate", thisMonthBeginString)
                        .queryParam("endDate", nextMonthBeginString)
                        .build())
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                })
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(TeamworkTimeTotalResponse.class)
                                .map(data -> new ApiResponse<>(
                                        data,
                                        null,
                                        res.statusCode().toString()
                                ));
                    }
                    else {
                        return res.bodyToMono(ErrorResponse.class)
                                .map(errors -> new ApiResponse<TeamworkTimeTotalResponse>(
                                        null,
                                        errors.errors(),
                                        res.statusCode().toString()
                                ));
                    }
                })
                .block();

        log.info("client response: {}", response);

        return response;
    }



}
