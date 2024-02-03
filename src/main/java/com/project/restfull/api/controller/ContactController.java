package com.project.restfull.api.controller;

import com.project.restfull.api.model.User;
import com.project.restfull.api.pojo.ContactResponse;
import com.project.restfull.api.pojo.CreateContactRequest;
import com.project.restfull.api.pojo.WebResponse;
import com.project.restfull.api.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class ContactController {
    @Autowired
    private ContactService contactService;

    @PostMapping(path = "/contacts", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<ContactResponse> createContact(User user, @RequestBody CreateContactRequest request) {
        ContactResponse contactResponse = contactService.createContact(user, request);
        WebResponse<ContactResponse> response = new WebResponse<>();
        response.setData(contactResponse);
        return response;
    }

    @GetMapping(path = "/contacts/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<ContactResponse> getContact(User user, @PathVariable("id") String id) {
        ContactResponse contactResponse = contactService.getContact(user, id);
        WebResponse<ContactResponse> response = new WebResponse<>();
        response.setData(contactResponse);
        return response;
    }
}
