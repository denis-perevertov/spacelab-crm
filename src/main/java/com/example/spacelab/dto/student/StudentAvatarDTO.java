package com.example.spacelab.dto.student;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentAvatarDTO {

    @Schema(example = "3")
    private Long id;
    @Schema(example = "StudentName")
    private String name;
    @Schema(example = "avatar.jpg")
    private String avatar;

}
