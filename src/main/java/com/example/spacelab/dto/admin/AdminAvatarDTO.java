package com.example.spacelab.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminAvatarDTO {

    private Long id;
    private String name;
    private String avatar;

}
