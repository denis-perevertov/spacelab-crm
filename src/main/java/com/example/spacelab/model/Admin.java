package com.example.spacelab.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="admins")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}
