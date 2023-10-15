package com.example.spacelab.dto.contact;

import com.example.spacelab.dto.admin.AdminContactDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ContactInfoDTO {

    @Schema(example = "10")
    private Long id;
    private String name;
    private AdminContactDTO admin;
    @Schema(example = "+380123456789")
    private String phone;
    @Schema(example = "testemail@gmail.com")
    private String email;
    @Schema(example = "@test")
    private String telegram;

}
