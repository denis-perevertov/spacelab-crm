package com.example.spacelab.dto.contact;

import lombok.Data;

@Data
public class ContactInfoEditDTO {

    private Long id;
    private Long adminID;
    private String phone;
    private String email;
    private String telegram;

}
