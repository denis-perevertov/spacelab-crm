package com.example.spacelab.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminAvatarDTO {

    private Long id;
    private String name;
    private String avatar;

    public AdminAvatarDTO(Long id, String name, String avatar) {
        this.id = id;
        this.name = name != null ? name.trim() : null;
        this.avatar = avatar != null ? avatar.trim() : null;
    }
}
