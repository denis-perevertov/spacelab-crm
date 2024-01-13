package com.example.spacelab.dto.contact;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ContactInfoEditDTO {

    private Long id;
    private String name;
    @Schema(example = "1")
    private Long adminID;
    private String adminName;
    private String adminAvatar;
    private String roleName;
    @Schema(example = "+380123456789")
    private String phone;
    @Schema(example = "testemail@gmail.com")
    private String email;
    @Schema(example = "@test")
    private String telegram;

}
