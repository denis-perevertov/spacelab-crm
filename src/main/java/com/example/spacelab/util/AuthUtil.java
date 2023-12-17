package com.example.spacelab.util;

import com.example.spacelab.model.admin.Admin;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.role.PermissionType;
import com.example.spacelab.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Log
@RequiredArgsConstructor
@Transactional
public class AuthUtil {

    private final AdminRepository adminRepository;

    public Admin getLoggedInAdmin() {
        Admin admin = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        admin = adminRepository.findById(admin.getId()).orElseThrow();
        return admin;
    }

    public PermissionType getPermission(String permissionName) {
        return getLoggedInAdmin().getRole().getPermission(permissionName);
    }

    public void checkAccessToCourse(Long courseID, String permissionName) {
        if(courseID == null) return;
        Admin admin = getLoggedInAdmin();
        PermissionType permissionToCheck = getPermission(permissionName);
        if(permissionToCheck == PermissionType.FULL) return;
        else if(permissionToCheck == PermissionType.PARTIAL) {
            if(!admin.getCourses().stream().map(Course::getId).toList().contains(courseID))
                throw new AccessDeniedException("No access to creating new students for this course! (courseID: "+courseID+") !");
        }
        else throw new AccessDeniedException("No access to creating new students for this course! (courseID: "+courseID+") !");
    }

    public void checkPermissionToCreateCourse() {
        PermissionType createCoursePermission = getLoggedInAdmin().getRole()
                .getPermissions().getWriteCourses();

        if(createCoursePermission == PermissionType.NO_ACCESS)
            throw new AccessDeniedException("No access to creating courses!");
    }

}
