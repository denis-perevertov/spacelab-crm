package com.example.spacelab.validator;

import com.example.spacelab.model.dto.role.UserRoleEditDTO;
import com.example.spacelab.model.role.UserRole;
import com.example.spacelab.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RoleValidator implements Validator {

    private final UserRoleRepository roleRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserRole.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors e) {
        UserRoleEditDTO dto = (UserRoleEditDTO) target;

        if(dto.getName() == null || dto.getName().isEmpty())
            e.rejectValue("name", "name.empty", "Enter name!");
        else if(dto.getName().length() < 2 || dto.getName().length() > 50)
            e.rejectValue("name", "name.length", "Name length: 2-50");
        else if(roleRepository.existsByName(dto.getName()) && !Objects.equals(roleRepository.findByName(dto.getName()).getId(), dto.getId()))
            e.rejectValue("name", "name.exists", "Role with that name already exists!");

    }
}
