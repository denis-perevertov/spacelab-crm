package com.example.spacelab.integration;

import com.example.spacelab.integration.data.*;

import java.util.List;

/*
        Course = Teamwork Project
        CourseTask = Teamwork TaskList
        CourseTaskProgressPoint = Teamwork Task
 */

public interface TaskTrackingService {

        UserResponse createTaskUser(UserCreateRequest request);
        UserAddResponse addUsersToProject(UserAddRequest request);
        UserRemoveResponse removeUsersFromProject(UserRemoveRequest request);
        // todo add users to project

        ProjectResponse createProject(ProjectRequest request);
        ProjectResponse updateProject(ProjectRequest request);
        void deleteProject(String projectId);

        TaskListResponse createTaskList(TaskListRequest request);
        TaskListResponse updateTaskList(TaskListRequest request);
        void deleteTaskList(String taskListId);

        List<TaskResponse> getAllTasksFromList(String taskListId);
        TaskResponse createTaskInTaskList(TaskRequest request);
        TaskResponse getTask(String taskId);
        TaskResponse updateTask(TaskRequest request);
        void deleteTask(String taskId);
        void completeTask(String taskId);
        void uncompleteTask(String taskId);

        List<TaskResponse> getTaskSubtasks(String taskId);
        TaskResponse createTaskSubtask(TaskRequest request);

        List<TimeEntryResponse> getAllTimeEntriesForTask(String taskId);
        // todo get user time entries or user time total
        TimeEntryResponse getTimeEntry(String timelogId);
        TimeEntryResponse createTimeEntryForTask(TimeEntryRequest request);
        TimeEntryResponse updateTimeEntryForTask(TimeEntryRequest request);
        TimeEntryResponse createTimeEntryForProject(TimeEntryRequest request);
        void deleteTimeEntry(String timelogId);

        TimeTotalResponse getTotalTimeOnTaskList(String taskListId);
        TimeTotalResponse getTotalTimeOnTask(String taskId);

        TagResponse getTagByName(String tagName);
        TagResponse getTagById(String tagId);
        String getRecommendedTagName();

}
