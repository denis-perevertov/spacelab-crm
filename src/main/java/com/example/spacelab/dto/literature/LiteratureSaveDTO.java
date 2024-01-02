package com.example.spacelab.dto.literature;

import com.example.spacelab.model.literature.LiteratureType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LiteratureSaveDTO {
    @Schema(example = "3")
    private Long id;

    @Schema(example = "LiteratureName")
    private String name;

    @Schema(example = "3")
    private Long courseID;

    @Schema(example = "BOOK")
    private LiteratureType type;

    @Schema(example = "AuthorName")
    private String author;

    @Schema(example = "keyword1, keyword2")
    private String keywords;

    @Schema(example = "Description")
    private String description;

    @Schema(example = "book.pdf")
    private String resource_link;

    private MultipartFile resource_file;

    private MultipartFile thumbnail;

    private boolean needs_verification;

    public LiteratureSaveDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
