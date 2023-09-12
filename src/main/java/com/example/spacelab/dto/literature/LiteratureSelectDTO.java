package com.example.spacelab.dto.literature;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LiteratureSelectDTO {
    @Schema(example = "3")
    private Long id;

    @Schema(example = "LiteratureName")
    private String name;

}
