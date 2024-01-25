package com.example.spacelab.validator;

import com.example.spacelab.model.contact.ContactInfo;
import com.example.spacelab.dto.contact.ContactInfoEditDTO;
import com.example.spacelab.repository.AdminRepository;
import com.example.spacelab.repository.ContactInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static com.example.spacelab.util.ValidationUtils.*;

@Component
@RequiredArgsConstructor
public class ContactValidator implements Validator {

    private final static String PHONE_PATTERN = "^([+]?[\\s0-9]+)?(\\d{3}|[(]?[0-9]+[)])?([-]?[\\s]?[0-9])+$";
    private final static String EMAIL_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private final static String TELEGRAM_PATTERN = "^@[A-Za-z0-9_.]{3,}$";

    private final AdminRepository adminRepository;
    private final ContactInfoRepository contactRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return ContactInfo.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors e) {
        ContactInfoEditDTO dto = (ContactInfoEditDTO) target;

        if(fieldIsNotEmpty(dto.getEmail())) {
            if(fieldLengthIsIncorrect(dto.getEmail(), 2, 50)) {
                e.rejectValue("email", "email.length", "Email length : 2-50");
            }
            else if(!fieldMatchesPattern(dto.getEmail(), PHONE_PATTERN)) {
                e.rejectValue("email", "email.no-match", "Incorrect email format!");
            }
        }

        if(fieldIsNotEmpty(dto.getPhone())) {
            if(fieldLengthIsIncorrect(dto.getPhone(), 9, 20)) {
                e.rejectValue("phone", "phone.length", "Phone length : 9-20");
            }
            else if(!fieldMatchesPattern(dto.getPhone(), PHONE_PATTERN)) {
                e.rejectValue("phone", "phone.no-match", "Incorrect phone format!");
            }
        }

        if(fieldIsNotEmpty(dto.getTelegram()) && !fieldMatchesPattern(dto.getTelegram(), TELEGRAM_PATTERN)) {
            e.rejectValue("telegram", "telegram.no-match", "Incorrect telegram format!");
        }

        if(fieldIsNotEmpty(dto.getTelegram())) {
            if(fieldLengthIsIncorrect(dto.getTelegram(), 2, 50)) {
                e.rejectValue("telegram", "telegram.length", "Length: 2-50");
            }
            else if(!fieldMatchesPattern(dto.getTelegram(), TELEGRAM_PATTERN)) {
                e.rejectValue("telegram", "telegram.no-match", "Incorrect telegram format!");
            }
        }

        if(fieldIsEmpty(dto.getAdminID())) {
            e.rejectValue("adminID", "adminID.empty", "Select admin!");
        }
        else if(!adminExists(dto.getAdminID()))
            e.rejectValue("adminID", "adminID.no-match", "Admin with this ID doesn't exist!");

    }

    private boolean adminExists(Long adminId) {
        return adminRepository.existsById(adminId);
    }
}
