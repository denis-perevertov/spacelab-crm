package com.example.spacelab.dto.literature;

import com.example.spacelab.model.literature.LiteratureType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LiteratureInfoDTO {

    @Schema(example = "3")
    private Long id;

    @Schema(example = "LiteratureName")
    private String name;

    @Schema(example = "3")
    private Long courseID;

    @Schema(example = "CourseName")
    private String courseName;

    @Schema(example = "BOOK")
    private LiteratureType type;

    @Schema(example = "AuthorName")
    private String author;

    @Schema(example = "keyword1, keyword2")
    private String keywords;

    private String resource_file;

    @Schema(example = "book.pdf")
    private String resource_link;

    @Schema(example = "Description")
    private String description;

    @Schema(example = "book_thumbnail.png")
    private String thumbnail;

    private boolean verified;

    public LiteratureInfoDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
