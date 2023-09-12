package com.example.spacelab.dto.role;

import com.example.spacelab.dto.student.StudentAvatarDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class UserRoleInfoDTO {

    @Schema(example = "3")
    private Long id;
    @Schema(example = "RoleName")
    private String name;
    private List<StudentAvatarDTO> users;
}
