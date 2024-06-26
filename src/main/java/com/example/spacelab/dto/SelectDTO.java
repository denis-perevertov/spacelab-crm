package com.example.spacelab.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectDTO {

    @Schema(example = "10")
    private String id;

    @Schema(example = "name")
    private String name;

}
