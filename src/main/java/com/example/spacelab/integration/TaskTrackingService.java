package com.example.spacelab.integration;

import com.example.spacelab.integration.data.*;

import java.util.Optional;

public interface TaskTrackingService {

        Optional<TaskResponse> getTaskById(String taskId);
        UserResponse createTaskUser(UserCreateRequest request);

        TaskListResponse createTaskList(TaskListCreateRequest request);
//        void deleteTaskListById(String taskListId);
//
//        void createTaskInTaskList(String taskListId);
//        void getAllTasksFromList(String taskListId);
//
//        void updateTaskById(String taskId);
//        void deleteTaskById(String taskId);
//
//        void getTaskSubtasks(String taskId);
//        void createTaskSubtask(String taskId);
//
//        void getAllTimeEntriesForTask(String taskId);
//        void getTimeEntryForTask(String taskId);
//        void createTimeEntryForTask(String taskId);
//        void updateTimeEntryForTask(String taskId);
//        void deleteTimeEntryForTask(String taskId);
//
//        void getTotalTimeOnTaskList(String taskListId);
//        void getTotalTimeOnTask(String taskId);

}
