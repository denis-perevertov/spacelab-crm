package com.example.spacelab.dto.contact;


import com.example.spacelab.dto.admin.AdminContactDTO;
import lombok.Data;

@Data
public class ContactInfoDTO {

    private Long id;
    private AdminContactDTO admin;
    private String phone;
    private String email;
    private String telegram;

}
