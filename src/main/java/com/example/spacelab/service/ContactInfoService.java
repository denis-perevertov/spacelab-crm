package com.example.spacelab.service;

import com.example.spacelab.model.contact.ContactInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContactInfoService {

    List<ContactInfo> getContacts();
    Page<ContactInfo> getContactsForRole(Long role, Pageable pageable);
    Page<ContactInfo> getContacts(Pageable pageable);

    ContactInfo getContact(Long id);
    ContactInfo saveContact(ContactInfo info);
    ContactInfo editContact(ContactInfo info);

    void deleteContact(Long id);
}
