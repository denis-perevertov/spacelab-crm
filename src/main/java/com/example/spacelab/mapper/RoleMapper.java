package com.example.spacelab.mapper;

import com.example.spacelab.exception.MappingException;
import com.example.spacelab.model.role.PermissionSet;
import com.example.spacelab.model.role.UserRole;
import com.example.spacelab.dto.UserRoleDTO;
import com.example.spacelab.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

@Component
@Log
@RequiredArgsConstructor
public class RoleMapper {

    private final UserRoleRepository roleRepository;

    public UserRoleDTO fromRoleToDTO(UserRole role) {
        UserRoleDTO dto = new UserRoleDTO();

        try {
            dto.setId(role.getId());
            dto.setName(role.getName());
            dto.setPermissions(role.getPermissions());

        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("DTO: " + dto);
            throw new MappingException(e.getMessage());
        }


        return dto;
    }

    public UserRole fromDTOToRole(UserRoleDTO dto) {

        UserRole role = (dto.getId() == null) ? new UserRole()
                : roleRepository.getReferenceById(dto.getId());

        try {

            role.setName(dto.getName());
            PermissionSet permissions = dto.getPermissions();

            /*
            permissions.setReadStatistics((permissions.getReadStatistics() != null) ? permissions.getReadStatistics() : PermissionType.UNDEFINED);

            permissions.setReadStatistics((permissions.getReadStatistics() != null) ? permissions.getReadStatistics() : PermissionType.UNDEFINED);
            permissions.setReadStatistics((permissions.getReadStatistics() != null) ? permissions.getReadStatistics() : PermissionType.UNDEFINED);
            permissions.setReadStatistics((permissions.getReadStatistics() != null) ? permissions.getReadStatistics() : PermissionType.UNDEFINED);
            permissions.setReadStatistics((permissions.getReadStatistics() != null) ? permissions.getReadStatistics() : PermissionType.UNDEFINED);

            permissions.setReadStatistics((permissions.getReadStatistics() != null) ? permissions.getReadStatistics() : PermissionType.UNDEFINED);
            permissions.setReadStatistics((permissions.getReadStatistics() != null) ? permissions.getReadStatistics() : PermissionType.UNDEFINED);
            permissions.setReadStatistics((permissions.getReadStatistics() != null) ? permissions.getReadStatistics() : PermissionType.UNDEFINED);
            permissions.setReadStatistics((permissions.getReadStatistics() != null) ? permissions.getReadStatistics() : PermissionType.UNDEFINED);
            permissions.setReadStatistics((permissions.getReadStatistics() != null) ? permissions.getReadStatistics() : PermissionType.UNDEFINED);

            permissions.setReadStatistics((permissions.getReadStatistics() != null) ? permissions.getReadStatistics() : PermissionType.UNDEFINED);
            permissions.setReadStatistics((permissions.getReadStatistics() != null) ? permissions.getReadStatistics() : PermissionType.UNDEFINED);
            permissions.setReadStatistics((permissions.getReadStatistics() != null) ? permissions.getReadStatistics() : PermissionType.UNDEFINED);
            permissions.setReadStatistics((permissions.getReadStatistics() != null) ? permissions.getReadStatistics() : PermissionType.UNDEFINED);

            permissions.setReadStatistics((permissions.getReadStatistics() != null) ? permissions.getReadStatistics() : PermissionType.UNDEFINED);
            permissions.setReadStatistics((permissions.getReadStatistics() != null) ? permissions.getReadStatistics() : PermissionType.UNDEFINED);
            permissions.setReadStatistics((permissions.getReadStatistics() != null) ? permissions.getReadStatistics() : PermissionType.UNDEFINED);
            permissions.setReadStatistics((permissions.getReadStatistics() != null) ? permissions.getReadStatistics() : PermissionType.UNDEFINED);

            permissions.setReadStatistics((permissions.getReadStatistics() != null) ? permissions.getReadStatistics() : PermissionType.UNDEFINED);
            permissions.setReadStatistics((permissions.getReadStatistics() != null) ? permissions.getReadStatistics() : PermissionType.UNDEFINED);
            permissions.setReadStatistics((permissions.getReadStatistics() != null) ? permissions.getReadStatistics() : PermissionType.UNDEFINED);
            permissions.setReadStatistics((permissions.getReadStatistics() != null) ? permissions.getReadStatistics() : PermissionType.UNDEFINED);
            permissions.setReadStatistics((permissions.getReadStatistics() != null) ? permissions.getReadStatistics() : PermissionType.UNDEFINED);

            permissions.setReadStatistics((permissions.getReadStatistics() != null) ? permissions.getReadStatistics() : PermissionType.UNDEFINED);
            permissions.setReadStatistics((permissions.getReadStatistics() != null) ? permissions.getReadStatistics() : PermissionType.UNDEFINED);
            permissions.setReadStatistics((permissions.getReadStatistics() != null) ? permissions.getReadStatistics() : PermissionType.UNDEFINED);
            permissions.setReadStatistics((permissions.getReadStatistics() != null) ? permissions.getReadStatistics() : PermissionType.UNDEFINED);

            permissions.setReadStatistics((permissions.getReadStatistics() != null) ? permissions.getReadStatistics() : PermissionType.UNDEFINED);
            permissions.setReadStatistics((permissions.getReadStatistics() != null) ? permissions.getReadStatistics() : PermissionType.UNDEFINED);
            */

            role.setPermissions(permissions);

        } catch (Exception e) {
            log.severe("Mapping error: " + e.getMessage());
            log.warning("Entity: " + role);
            throw new MappingException(e.getMessage());
        }

        return role;
    }
}
