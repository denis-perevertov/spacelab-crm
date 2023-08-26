package com.example.spacelab.model.role;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Embeddable
@Data
public class PermissionSet {

    // список данных разрешений
    @Transient List<String> authorities;

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
    PermissionType writeSettings;
    PermissionType editSettings;
    PermissionType deleteSettings;

    public PermissionType getPermission(String permissionName) {
        return switch(permissionName) {
            case "statistics.read" -> this.readStatistics;
            case "tasks.read" -> this.readTasks;
            case "tasks.write" -> this.writeTasks;
            case "tasks.edit" -> this.editTasks;
            case "tasks.delete" -> this.deleteTasks;
            case "students.read" -> this.readStudents;
            case "students.write" -> this.writeStudents;
            case "students.edit" -> this.editStudents;
            case "students.delete" -> this.deleteStudents;
            case "courses.read" -> this.readCourses;
            case "courses.write" -> this.writeCourses;
            case "courses.edit" -> this.editCourses;
            case "courses.delete" -> this.deleteCourses;
            case "lessons.read" -> this.readLessons;
            case "lessons.write" -> this.writeLessons;
            case "lessons.edit" -> this.editLessons;
            case "lessons.delete" -> this.deleteLessons;
            case "literatures.read" -> this.readLiteratures;
            case "literatures.write" -> this.writeLiteratures;
            case "literatures.edit" -> this.editLiteratures;
            case "literatures.delete" -> this.deleteLiteratures;
            case "roles.read" -> this.readRoles;
            case "roles.write" -> this.writeRoles;
            case "roles.edit" -> this.editRoles;
            case "roles.delete" -> this.deleteRoles;
            case "settings.read" -> this.readSettings;
            case "settings.write" -> this.writeSettings;
            case "settings.edit" -> this.editSettings;
            case "settings.delete" -> this.deleteSettings;
            default -> null;
        };
    }

    public List<String> getAuthorities() {
        List<String> authorities = List.of(
                "statistics.read." + this.readStatistics.name(),
                "tasks.read." + this.readTasks.name(),
                "tasks.write." + this.writeTasks.name(),
                "tasks.edit." + this.editTasks.name(),
                "tasks.delete." + this.deleteTasks.name(),
                "students.read." + this.readStudents.name(),
                "students.write." + this.writeStudents.name(),
                "students.invite."+ this.inviteStudents.name(),
                "students.edit." + this.editStudents.name(),
                "students.delete." + this.deleteStudents.name(),
                "courses.read." + this.readCourses.name(),
                "courses.write." + this.writeCourses.name(),
                "courses.edit." + this.editCourses.name(),
                "courses.delete." + this.deleteCourses.name(),
                "lessons.read." + this.readLessons.name(),
                "lessons.write." + this.writeLessons.name(),
                "lessons.edit." + this.editLessons.name(),
                "lessons.delete." + this.deleteLessons.name(),
                "literatures.read." + this.readLiteratures.name(),
                "literatures.write." + this.writeLiteratures.name(),
                "literatures.verify." + this.verifyLiteratures.name(),
                "literatures.edit." + this.editLiteratures.name(),
                "literatures.delete." + this.deleteLiteratures.name(),
                "roles.read." + this.readRoles.name(),
                "roles.write." + this.writeRoles.name(),
                "roles.edit." + this.editRoles.name(),
                "roles.delete." + this.deleteRoles.name(),
                "settings.read." + this.readSettings.name(),
                "settings.write." + this.writeSettings.name(),
                "settings.edit." + this.editSettings.name(),
                "settings.delete." + this.deleteSettings.name()
        );
        System.out.println(authorities);
        return authorities;
    }
}
