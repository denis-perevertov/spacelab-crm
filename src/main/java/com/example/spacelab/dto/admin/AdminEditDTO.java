package com.example.spacelab.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class AdminEditDTO {

    private Long id;
    @Schema(example = "New")
    private String firstName;
    @Schema(example = "Admin")
    private String lastName;
    @Schema(example = "+380123456789")
    private String phone;
    @Schema(example = "test@gmail.com")
    private String email;
    @Schema(example = "12345678")
    private String password;
    @Schema(example = "12345678")
    private String confirmPassword;

    private String avatar;
    private MultipartFile avatarToSave;

    @Schema(example = "1")
    private Long roleID;
    private String roleName;
    @Schema(example = "4")
    private Long[] courseID;

    public AdminEditDTO(Long id, String firstName, String lastName, String phone, String email, String password, String confirmPassword, String avatar, MultipartFile avatarToSave, Long roleID, Long[] courseID) {
        this.id = id;
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.phone = phone.trim();
        this.email = email.trim();
        this.password = password.trim();
        this.confirmPassword = confirmPassword.trim();
        this.avatar = avatar.trim();
        this.avatarToSave = avatarToSave;
        this.roleID = roleID;
        this.courseID = courseID;
    }
}
