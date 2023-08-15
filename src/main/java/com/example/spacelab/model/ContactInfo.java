package com.example.spacelab.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="contacts")
public class ContactInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Admin admin;

    private String phone;
    private String email;
    private String telegram;
}
