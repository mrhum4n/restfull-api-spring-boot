package com.project.restfull.api.service.impl;

import com.project.restfull.api.model.Contact;
import com.project.restfull.api.model.User;
import com.project.restfull.api.pojo.ContactResponse;
import com.project.restfull.api.pojo.CreateContactRequest;
import com.project.restfull.api.repository.ContactRepo;
import com.project.restfull.api.service.ContactService;
import com.project.restfull.api.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    private ContactRepo contactRepo;
    @Autowired
    private ValidationService validationService;

    @Override
    public ContactResponse createContact(User user, CreateContactRequest request) {
        validationService.validation(request);

        Contact contact = new Contact();
        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setPhone(request.getPhone());
        contact.setEmail(request.getEmail());
        contact.setUser(user);
        contactRepo.save(contact);

        ContactResponse response = new ContactResponse();
        response.setId(contact.getId());
        response.setFirstName(contact.getFirstName());
        response.setLastName(contact.getLastName());
        response.setPhone(contact.getPhone());
        response.setEmail(contact.getEmail());

        return response;
    }
}
