package com.example.spacelab.model.contact;

import com.example.spacelab.model.admin.Admin;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="contacts")
public class ContactInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Admin admin;

    private String phone;
    private String email;
    private String telegram;
}
