package com.project.restfull.api.controller;

import com.project.restfull.api.model.User;
import com.project.restfull.api.pojo.*;
import com.project.restfull.api.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(path = "/contacts", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<List<ContactResponse>> searchContact(User user,
                                                            @RequestParam(value = "name", required = false) String name,
                                                            @RequestParam(value = "email", required = false) String email,
                                                            @RequestParam(value = "phone", required = false) String phone,
                                                            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                            @RequestParam(value = "size", required = false, defaultValue = "5") Integer size) {
        SearchContactRequest request = new SearchContactRequest();
        request.setName(name);
        request.setEmail(email);
        request.setPhone(phone);
        request.setPage(page);
        request.setSize(size);

        Page<ContactResponse> responses = contactService.searchContact(user, request);

        WebResponse<List<ContactResponse>> webResponse = new WebResponse<>();
        webResponse.setData(responses.getContent());
        PagingResponse pagingResponse = new PagingResponse();
        pagingResponse.setCurrentPage(responses.getNumber());
        pagingResponse.setSize(responses.getSize());
        pagingResponse.setTotalPage(responses.getTotalPages());
        webResponse.setPaging(pagingResponse);

        return webResponse;
    }
}
