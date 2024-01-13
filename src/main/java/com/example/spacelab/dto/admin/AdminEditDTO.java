package com.example.spacelab.dto.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
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

    @Schema(example = "1")
    private Long roleID;
    @Schema(example = "4")
    private Long[] courseID;

}
