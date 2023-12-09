package com.example.spacelab.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FilterForm {
    @Schema(defaultValue = "Test")
    private String name;
    @Schema(defaultValue = "0")
    private Long course;
    @Schema(defaultValue = "test@gmail.com")
    private String email;
    @Schema(defaultValue = "+380123456789")
    private String phone;
    @Schema(defaultValue = "@test")
    private String telegram;
    @Schema(defaultValue = "0")
    private Integer rating;
    @Schema(defaultValue = "TEST")
    private String status;
    private String level;
    @Schema(defaultValue = "Test")
    private String type;
    @Schema(defaultValue = "Test")
    private String keywords;
    @Schema(defaultValue = "0")
    private Long role;
    private String begin;
    private String end;
    private String date;
    @Schema(defaultValue = "0", description = "Mentor's ID")
    private Long mentor;
    @Schema(defaultValue = "0", description = "Manager's ID")
    private Long manager;
    private String combined;
    private Boolean active;

    private String nameAndAuthor;
    private Boolean verified;

    public static FilterBuilder with() { return new FilterBuilder(); }
}
