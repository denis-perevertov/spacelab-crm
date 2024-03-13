package com.example.spacelab.dto.contact;

import com.example.spacelab.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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

    public ContactInfoEditDTO(Long id, String name, Long adminID, String adminName, String adminAvatar, String roleName, String phone, String email, String telegram) {
        this.id = id;
        this.name = StringUtils.trimString(name);
        this.adminID = adminID;
        this.adminName = StringUtils.trimString(adminName);
        this.adminAvatar = StringUtils.trimString(adminAvatar);
        this.roleName = StringUtils.trimString(roleName);
        this.phone = StringUtils.trimString(phone);
        this.email = StringUtils.trimString(email);
        this.telegram = StringUtils.trimString(telegram);
    }
}
