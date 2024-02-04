package com.project.restfull.api.service;

import com.project.restfull.api.model.User;
import com.project.restfull.api.pojo.ContactResponse;
import com.project.restfull.api.pojo.CreateContactRequest;
import com.project.restfull.api.pojo.SearchContactRequest;
import com.project.restfull.api.pojo.UpdateContactRequest;
import org.springframework.data.domain.Page;

public interface ContactService {
    ContactResponse createContact(User user, CreateContactRequest request);
    ContactResponse getContact(User user, String id);
    ContactResponse updateContact(User user, UpdateContactRequest request);
    void deleteContact(User user, String id);
    Page<ContactResponse> searchContact(User user, SearchContactRequest request);
}
