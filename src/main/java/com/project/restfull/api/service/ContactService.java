package com.project.restfull.api.service;

import com.project.restfull.api.model.User;
import com.project.restfull.api.pojo.ContactResponse;
import com.project.restfull.api.pojo.CreateContactRequest;

public interface ContactService {
    ContactResponse createContact(User user, CreateContactRequest request);
}
