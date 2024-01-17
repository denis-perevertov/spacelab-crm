package com.example.spacelab.integration.teamwork;

import com.example.spacelab.config.TeamworkProperties;
import com.example.spacelab.integration.data.*;
import com.example.spacelab.integration.teamwork.data.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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

    // todo use teamwork class
    public ApiResponse<UserResponse> createUser(UserCreateRequest request) {

        log.info("client request: {}", request);

        WebClient client = clientBuilder.build();

        ApiResponse<UserResponse> response = client.post()
                .uri(properties.getBaseUrl() + "/people.json")
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return res.bodyToMono(new ParameterizedTypeReference<ApiResponse<UserResponse>>() {
                        });
                    }
                    else {
                        return res.createException().flatMap(Mono::error);
                    }
                })
                .block();

        log.info("client response: {}", response);

        return response;
    }

    public ApiResponse<TeamworkTaskListCreateResponse> createTaskList(TaskListCreateRequest request) {

        log.info("client request: {}", request);

        WebClient client = clientBuilder.build();

        ApiResponse<TeamworkTaskListCreateResponse> response = client.post()
                .uri(properties.getBaseUrl() +
                        "/projects/" + properties.getProjectId() +
                        "/tasklists.json")
                .headers(headers -> {
                    headers.setBasicAuth(properties.getToken(), "");
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchangeToMono(res -> {
                    log.info("-- STATUS CODE : {} --", res);
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
                }).exchangeToMono(res -> {
                    if(res.statusCode().is2xxSuccessful()) {
                        return Mono.empty().map(empty -> new ApiResponse<Void>(
                                null,
                                null,
                                res.statusCode().toString()
                        ));
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
                .block();

        log.info("client response: {}", response);

        return response;
    }

    public ApiResponse<TeamworkTaskResponse> createNewTaskInList(TeamworkTaskCreateRequest request) {
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

    public ApiResponse<TeamworkTaskResponse> updateTask(String taskId, TeamworkTaskCreateRequest request) {

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
                        return Mono.empty().map(empty -> new ApiResponse<Void>(
                                null,
                                null,
                                res.statusCode().toString()
                        ));
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
                .block();

        log.info("client response: {}", response);

        return response;
    }

    public ApiResponse<TeamworkTaskListResponse> getAllTasksFromTaskList(String taskListId) {

        log.info("client request: {}", taskListId);

        WebClient client = clientBuilder.build();

        ApiResponse<TeamworkTaskListResponse> response = client.get()
                .uri(properties.getBaseUrl() +
                        "/projects/api/" + properties.getApiVersion() +
                        "/tasklists/" + taskListId + "/tasks.json")
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

    public ApiResponse<TeamworkTagResponse> getTagBySearchTerm(String searchTerm) {

        log.info("client request: {}", searchTerm);

        WebClient client = clientBuilder.build();
        String uri = properties.getBaseUrl() +
                "/projects/api/" + properties.getApiVersion() +
                "/tags.json?searchTerm="+searchTerm;

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

    public ApiResponse<TeamworkTaskResponse> createSubtask(TeamworkTaskCreateRequest request) {
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
                        return Mono.empty().map(empty -> new ApiResponse<Void>(
                                null,
                                null,
                                res.statusCode().toString()
                        ));
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





}
