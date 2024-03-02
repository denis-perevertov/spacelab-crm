package com.example.spacelab.validator;

import com.example.spacelab.dto.chat.ChatGroupSaveDTO;
import com.example.spacelab.model.chat.Chat;
import com.example.spacelab.util.ValidationUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ChatValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public void validate(Object target, Errors e) {
        ChatGroupSaveDTO chat = (ChatGroupSaveDTO) target;

        if(ValidationUtils.fieldIsEmpty(chat.name())) {
            e.rejectValue("name", "name.empty", "validation.field.empty");
        }
        else if(ValidationUtils.fieldMaxLengthIsIncorrect(chat.name(), 100)) {
            e.rejectValue("name", "name.length.max", "validation.field.length.max");
        }
    }
}
