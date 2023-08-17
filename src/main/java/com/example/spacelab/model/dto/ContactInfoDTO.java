package com.example.spacelab.model.dto;

import lombok.Data;

@Data
public class ContactInfoDTO {

    private Long id;
    private AdminDTO admin;
    private String phone;
    private String email;
    private String telegram;

}
