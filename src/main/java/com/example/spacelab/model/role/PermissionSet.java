package com.example.spacelab.model.role;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Embeddable
@Data
public class PermissionSet {

    // статистика
    PermissionType readStatistics;

    // задания
    PermissionType readTasks;
    PermissionType writeTasks;
    PermissionType editTasks;
    PermissionType deleteTasks;

    // студенты
    PermissionType readStudents;
    PermissionType writeStudents;
    PermissionType inviteStudents;
    PermissionType editStudents;
    PermissionType deleteStudents;

    // курсы
    PermissionType readCourses;
    PermissionType writeCourses;
    PermissionType editCourses;
    PermissionType deleteCourses;

    // занятия
    PermissionType readLessons;
    PermissionType writeLessons;
    PermissionType editLessons;
    PermissionType deleteLessons;

    // литература
    PermissionType readLiteratures;
    PermissionType writeLiteratures;
    PermissionType editLiteratures;
    PermissionType verifyLiteratures;
    PermissionType deleteLiteratures;

    // роли
    PermissionType readRoles;
    PermissionType writeRoles;
    PermissionType editRoles;
    PermissionType deleteRoles;

    // настройки системы
    PermissionType readSettings;
    PermissionType editSettings;
}
