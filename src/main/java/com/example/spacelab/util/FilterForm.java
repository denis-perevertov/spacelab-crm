package com.example.spacelab.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
@Data
public class FilterForm {

    private Long id;
    @Schema(defaultValue = "Test", description = "Name")
    private String name;
    @Schema(defaultValue = "1", description = "Student's ID")
    private Long student;
    @Schema(defaultValue = "1", description = "Course ID")
    private Long course;
    @Schema(defaultValue = "test@gmail.com")
    private String email;
    @Schema(defaultValue = "+380123456789")
    private String phone;
    @Schema(defaultValue = "@test")
    private String telegram;
    @Schema(defaultValue = "0")
    private Integer rating;
    @Schema(defaultValue = "1")
    private Integer ratingFrom;
    @Schema(defaultValue = "10")
    private Integer ratingTo;
    @Schema(defaultValue = "TEST")
    private String status;
    @Schema(defaultValue = "TEST")
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
    private Long admin;
    private String combined;
    private Boolean active;

    private String nameAndAuthor;
    private Boolean verified;

    private Boolean present;
    private Long task;
    private Integer hoursFrom;
    private Integer hoursTo;
    private String note;
    private String comment;

    public static FilterBuilder with() { return new FilterBuilder(); }

    public FilterForm trim() {
        try {
            for(Field field : this.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if(field.getType().equals(String.class)) {
                    field.set(this, field.get(this) != null ? field.get(this).toString().trim() : null);
                }
                field.setAccessible(false);
            }
        } catch (Exception e) {
            log.error("Could not trim filters");
            log.error(e.getMessage());
        }

        return this;
    }
//
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        for(Field field : this.getClass().getFields()) {
//            field.setAccessible(true);
//            try {
//                sb.append(field.getName())
//                        .append("=")
//                        .append(field.get(this))
//                        .append(",");
//            } catch (IllegalAccessException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        if(sb.length() > 0) sb.deleteCharAt(sb.length()-1);
//        return sb.toString();
//    }
}
