package com.example.spacelab.model.dto.contact;

import lombok.Data;

@Data
public class ContactInfoEditDTO {

    private Long id;
    private Long adminID;
    private String phone;
    private String email;
    private String telegram;

}
