package com.example.spacelab.validator;

import com.example.spacelab.model.contact.ContactInfo;
import com.example.spacelab.dto.contact.ContactInfoEditDTO;
import com.example.spacelab.repository.AdminRepository;
import com.example.spacelab.repository.ContactInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

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

        if(dto.getPhone() == null || dto.getPhone().isEmpty())
            e.rejectValue("phone", "phone.empty", "Enter phone!");
        else if(!dto.getPhone().matches(PHONE_PATTERN))
            e.rejectValue("phone", "phone.no-match", "Incorrect phone format!");

        if(dto.getEmail() == null || dto.getEmail().isEmpty())
            e.rejectValue("email", "email.empty", "Enter email!");
        else if(!dto.getEmail().matches(EMAIL_PATTERN))
            e.rejectValue("email", "email.no-match", "Incorrect email format!");

        if(dto.getTelegram() == null || dto.getTelegram().isEmpty())
            e.rejectValue("telegram", "telegram.empty", "Enter telegram!");
        else if(!dto.getTelegram().matches(TELEGRAM_PATTERN))
            e.rejectValue("telegram", "telegram.no-match", "Incorrect telegram format!");

        if(dto.getAdminID() == null || dto.getAdminID() == 0)
            e.rejectValue("adminID", "adminID.empty", "Select admin!");
        else if(!adminRepository.existsById(dto.getAdminID()))
            e.rejectValue("adminID", "adminID.no-match", "Admin with this ID doesn't exist!");


    }
}
