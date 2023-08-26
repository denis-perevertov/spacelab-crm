package com.example.spacelab.model.role;

import com.example.spacelab.model.UserEntity;
import com.example.spacelab.model.role.PermissionSet;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name="roles")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany
    private List<UserEntity> userList = new ArrayList<>();

    @ToString.Exclude
    @JsonIgnore
    @Embedded
    private PermissionSet permissions = new PermissionSet();

    public List<String> getAuthorities() {
        return this.permissions.getAuthorities();
    }

    public PermissionType getPermission(String permissionName) {return this.permissions.getPermission(permissionName);}
}
