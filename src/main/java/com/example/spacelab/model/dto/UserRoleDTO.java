package com.example.spacelab.model.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserRoleDTO {

    private Long id;
    private String name;
    private Set<String> permissions;

}
