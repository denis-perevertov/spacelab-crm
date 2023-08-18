package com.example.spacelab.model.role;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.util.Set;

@Embeddable
@Data
public class PermissionSet {
    Set<PermissionSection> sections;
}
