package com.example.spacelab.integration;

import com.example.spacelab.integration.data.*;
import com.example.spacelab.integration.teamwork.TeamworkClient;
import com.example.spacelab.integration.teamwork.data.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TeamworkClientTest {

    @Autowired
    private TeamworkClient client;

    @Test
    void contextLoads() {
        assertThat(client).isNotNull();
    }

    @Test
    void userCreationTest() {

    }

    @Test
    void createTaskListTest() {

        TeamworkTaskListRequest request = new TeamworkTaskListRequest(
                null,
                false,
                new TaskListDescription(
                        "TestTaskDesc",
                        "TestTaskName"
                )
        );
        ApiResponse<TeamworkTaskListCreateResponse> response = client.createTaskList("1", request);
        assertThat(response.getData()).isNotNull();
        assertThat(response.getData().taskListId()).isNotNull();
    }

    @Test
    void createTaskInTaskListTest() {

        TeamworkTaskRequest request = new TeamworkTaskRequest(
                new TeamworkTaskRequest.Task(
                        "TestTaskName",
                        "TestTaskDescription",
                        null,
                        null,
                        new Long[0],
                        null,
                        LocalDate.now(),
                        null,
                        (long) 3265785,
                        new TeamworkTaskAssignees(
                                new Integer[0],
                                new Integer[0],
                                new Integer[]{472016}
                        )
                )
        );
        ApiResponse<TeamworkTaskResponse> response = client.updateTask(String.valueOf(40425827), request);
        assertThat(response.getData()).isNotNull();
        assertThat(response.getErrors()).isNotNull();
    }

    @Test
    void createTaskInTaskListWithRecommendedTagTest() {

    }

    @Test
    void updateTaskTest() {

        TeamworkTaskRequest request = new TeamworkTaskRequest(
                new TeamworkTaskRequest.Task(
                        "TestTaskNameEDITED222",
                        "TestTaskDescriptionEDITED222",
                        null,
                        20,
                        new Long[0],
                        null,
                        LocalDate.now(),
                        null,
                        (long) 3265785,
                        new TeamworkTaskAssignees(
                                new Integer[0],
                                new Integer[0],
                                new Integer[]{472016}
                        )
                )
        );
        ApiResponse<TeamworkTaskResponse> response = client.updateTask(String.valueOf(40425827), request);
        assertThat(response.getData()).isNotNull();
        assertThat(response.getErrors()).isNotNull();
    }

    @Test
    void deleteTaskWithCorrectIdTest() {
        assertThat(client.deleteTask(String.valueOf(40425842))).isNull();
    }

    @Test
    void deleteTaskWithIncorrectIdErrorsTest() {
        assertThat(client.deleteTask(String.valueOf(12345))).isNotNull();
    }

    @Test
    void getTasksFromTaskListTest() {
        String taskListId = String.valueOf(3270959);
        ApiResponse<TeamworkTaskListResponse> response = client.getAllTasksFromTaskList(taskListId);
        System.out.println(response.getData().tasks().stream().map(TeamworkTask::status).toList());
        assertThat(response.getErrors()).isNull();
        assertThat(response.getData()).isNotNull();
    }

    @Test
    void getRecommendedTagTest() {
        String tagName = "recommended";
        ApiResponse<TeamworkTagListResponse> response = client.getTagBySearchTerm(tagName);
        assertThat(response.getErrors()).isNull();
        assertThat(response.getData()).isNotNull();
    }

    @Test
    void getTaskSubtasksTest() {
        String taskId = String.valueOf(40425119);
        ApiResponse<TeamworkTaskListResponse> response = client.getTaskSubtasks(taskId);
        assertThat(response.getErrors()).isNull();
        assertThat(response.getData()).isNotNull();
    }

    @Test
    void createSubtaskTest() {
        Long parentTaskId = (long) 40425842;
        Long taskListId = (long) 3265785;
        TeamworkTaskRequest request = new TeamworkTaskRequest(
                new TeamworkTaskRequest.Task(
                        "TestTaskNameEDITED222",
                        "TestTaskDescriptionEDITED222",
                        null,
                        20,
                        new Long[0],
                        null,
                        LocalDate.now(),
                        parentTaskId,
                        taskListId,
                        new TeamworkTaskAssignees(
                                new Integer[0],
                                new Integer[0],
                                new Integer[]{472016}
                        )
                )
        );
        ApiResponse<TeamworkTaskResponse> response = client.createSubtask(request);
        assertThat(response).isNotNull();
    }

    @Test
    void getTaskTimeEntriesTest() {
        String taskId = String.valueOf(40425842);
        ApiResponse<TeamworkTimeEntryListResponse> response = client.getTaskTimeEntries(taskId);
        assertThat(response.getErrors()).isNull();
        assertThat(response.getData()).isNotNull();
    }

//    @Test
//    void getTaskSingleTimeEntryTest() {
//        String taskId = String.valueOf(40425119);
//        ApiResponse<TeamworkTimeEntryResponse> response = client.getTaskTimeEntries(taskId);
//        assertThat(response.getErrors()).isNull();
//        assertThat(response.getData()).isNotNull();
//    }

    @Test
    void createTaskTimeEntryTest() {
        Long taskId = (long) 40425842;
        Long userId = (long) 472016;
        TeamworkTimeEntryCreateRequest request = new TeamworkTimeEntryCreateRequest(
                new TimeEntry (
                        LocalDate.now(),
                        LocalTime.now(),
                        100,
                        6L,
                        taskId,
                        userId
                ),
                null,
                null
        );
        ApiResponse<TeamworkTimeEntryResponse> response = client.createTaskTimeEntry(request);
        assertThat(response.getErrors()).isNull();
        assertThat(response.getData()).isNotNull();
    }

    @Test
    void updateTaskTimeEntryTest() {
        String timelogId = String.valueOf(14077836);
        Long taskId = (long) 40425842;
        Long userId = (long) 472016;
        TeamworkTimeEntryCreateRequest request = new TeamworkTimeEntryCreateRequest(
                new TimeEntry(
                        LocalDate.now(),
                        LocalTime.now(),
                        100,
                        6L,
                        taskId,
                        userId
                ),
                null,
                null
        );
        ApiResponse<TeamworkTimeEntryResponse> response = client.updateTaskTimeEntry(timelogId, request);
        assertThat(response.getErrors()).isNull();
        assertThat(response.getData()).isNotNull();
    }

    @Test
    void deleteTaskTimeEntryTest() {
        String timelogId = String.valueOf(14077836);
        ApiResponse<Void> response = client.deleteTaskTimeEntry(timelogId);
        assertThat(response.getErrors()).isNotNull();
    }

    @Test
    void getTaskTimeTotalTest() {
        String taskId = String.valueOf(40425119);
        ApiResponse<TeamworkTimeTotalResponse> response = client.getTaskTimeTotal(taskId);
        assertThat(response.getErrors()).isNull();
        assertThat(response.getData()).isNotNull();
    }

    @Test
    void getTaskListTimeTotalTest() {
        String taskListId = String.valueOf(3265785);
        ApiResponse<TeamworkTimeTotalResponse> response = client.getTaskListTimeTotal(taskListId);
        assertThat(response.getErrors()).isNull();
        assertThat(response.getData()).isNotNull();
    }

}
