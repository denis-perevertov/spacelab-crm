package com.example.spacelab.dto.student;

import com.example.spacelab.util.StringUtils;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudentInviteRequestDTO {

    @Size(max = 255, message = "validation.field.length.max")
    private String firstName;
    @Size(max = 255, message = "validation.field.length.max")
    private String lastName;
    @Size(max = 255, message = "validation.field.length.max")
    private String fathersName;
    @Email(message = "validation.field.format.allowed")
    @Size(max = 255, message = "validation.field.length.max")
    private String email;
    @Pattern(regexp = "^((38)?0(99|50|66|97|96|98)\\d{7})?$", message = "validation.field.format.allowed")
    private String phone;
    @NotNull(message = "validation.field.select")
    @Min(0)
    private Long courseID;

    public StudentInviteRequestDTO(String firstName, String lastName, String fathersName, String email, String phone, Long courseID) {
        this.firstName = StringUtils.trimString(firstName);
        this.lastName = StringUtils.trimString(lastName);
        this.fathersName = StringUtils.trimString(lastName);
        this.email = StringUtils.trimString(email);
        this.phone = StringUtils.trimString(phone);
        this.courseID = courseID;
    }
}
