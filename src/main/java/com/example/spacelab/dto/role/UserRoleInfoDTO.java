package com.example.spacelab.dto.role;

import com.example.spacelab.dto.student.StudentAvatarDTO;
import lombok.Data;

import java.util.List;

@Data
public class UserRoleInfoDTO {

    private Long id;
    private String name;
    private List<StudentAvatarDTO> users;
}
