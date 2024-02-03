package com.project.restfull.api.controller;

import com.project.restfull.api.model.User;
import com.project.restfull.api.pojo.ContactResponse;
import com.project.restfull.api.pojo.CreateContactRequest;
import com.project.restfull.api.pojo.UpdateContactRequest;
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

    @PutMapping(path = "/contacts/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<ContactResponse> createContact(User user, @RequestBody UpdateContactRequest request, @PathVariable("id") String id) {
        request.setId(id);
        ContactResponse contactResponse = contactService.updateContact(user, request);
        WebResponse<ContactResponse> response = new WebResponse<>();
        response.setData(contactResponse);
        return response;
    }

    @DeleteMapping(path = "/contacts/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> deleteContact(User user, @PathVariable("id") String id) {
        contactService.deleteContact(user,id);
        WebResponse<String> response = new WebResponse<>();
        response.setData("Ok");
        return response;
    }
}
