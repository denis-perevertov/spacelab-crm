package com.example.spacelab.model.role;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class PermissionSet {

    // статистика
    PermissionType readStatistics = PermissionType.UNDEFINED;

    // задания
    PermissionType readTasks = PermissionType.UNDEFINED;
    PermissionType writeTasks = PermissionType.UNDEFINED;
    PermissionType editTasks = PermissionType.UNDEFINED;
    PermissionType deleteTasks = PermissionType.UNDEFINED;

    // студенты
    PermissionType readStudents = PermissionType.UNDEFINED;
    PermissionType writeStudents = PermissionType.UNDEFINED;
    PermissionType editStudents = PermissionType.UNDEFINED;
    PermissionType deleteStudents = PermissionType.UNDEFINED;

    // курсы
    PermissionType readCourses = PermissionType.UNDEFINED;
    PermissionType writeCourses = PermissionType.UNDEFINED;
    PermissionType editCourses = PermissionType.UNDEFINED;
    PermissionType deleteCourses = PermissionType.UNDEFINED;

    // занятия
    PermissionType readLessons = PermissionType.UNDEFINED;
    PermissionType writeLessons = PermissionType.UNDEFINED;
    PermissionType editLessons = PermissionType.UNDEFINED;
    PermissionType deleteLessons = PermissionType.UNDEFINED;

    // литература
    PermissionType readLiteratures = PermissionType.UNDEFINED;
    PermissionType writeLiteratures = PermissionType.UNDEFINED;
    PermissionType editLiteratures = PermissionType.UNDEFINED;
    PermissionType deleteLiteratures = PermissionType.UNDEFINED;

    // роли
    PermissionType readRoles = PermissionType.UNDEFINED;
    PermissionType writeRoles = PermissionType.UNDEFINED;
    PermissionType editRoles = PermissionType.UNDEFINED;
    PermissionType deleteRoles = PermissionType.UNDEFINED;

    // настройки системы
    PermissionType readSettings = PermissionType.UNDEFINED;
    PermissionType editSettings = PermissionType.UNDEFINED;
}
