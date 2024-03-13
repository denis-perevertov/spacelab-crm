package com.example.spacelab.dto.literature;

import com.example.spacelab.model.literature.LiteratureType;
import com.example.spacelab.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
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

    public LiteratureSaveDTO(Long id, String name, Long courseID, LiteratureType type, String author, String keywords, String description, String resource_link, MultipartFile resource_file, MultipartFile thumbnail, boolean needs_verification) {
        this.id = id;
        this.name = StringUtils.trimString(name);
        this.courseID = courseID;
        this.type = type;
        this.author = StringUtils.trimString(author);
        this.keywords = StringUtils.trimString(keywords);
        this.description = StringUtils.trimString(description);
        this.resource_link = StringUtils.trimString(resource_link);
        this.resource_file = resource_file;
        this.thumbnail = thumbnail;
        this.needs_verification = needs_verification;
    }
}
