package com.example.spacelab.model.dto.contact;

import com.example.spacelab.model.dto.admin.AdminDTO;
import com.example.spacelab.model.dto.admin.ContactAdminDTO;
import lombok.Data;

@Data
public class ContactInfoDTO {

    private Long id;
    private ContactAdminDTO admin;
    private String phone;
    private String email;
    private String telegram;

}
