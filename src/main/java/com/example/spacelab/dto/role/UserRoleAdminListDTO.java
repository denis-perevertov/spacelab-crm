package com.example.spacelab.dto.role;

import com.example.spacelab.dto.admin.AdminAvatarDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleAdminListDTO {

    private Long id;
    private String name;
    private List<AdminAvatarDTO> admins;

}
