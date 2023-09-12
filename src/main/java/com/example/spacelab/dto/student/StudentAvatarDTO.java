package com.example.spacelab.dto.student;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class StudentAvatarDTO {

    @Schema(example = "3")
    private Long id;
    @Schema(example = "StudentName")
    private String name;
    @Schema(example = "avatar.jpg")
    private String avatar;

}
