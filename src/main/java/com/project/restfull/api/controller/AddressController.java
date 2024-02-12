package com.project.restfull.api.controller;

import com.project.restfull.api.model.User;
import com.project.restfull.api.pojo.AddressResponse;
import com.project.restfull.api.pojo.CreateAddressRequest;
import com.project.restfull.api.pojo.WebResponse;
import com.project.restfull.api.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @PostMapping("/contact/{contactId}/addresses")
    public WebResponse<AddressResponse> create(User user, @RequestBody CreateAddressRequest request, @PathVariable("contactId") String contactId) {
        request.setContactId(contactId);
        AddressResponse response = addressService.create(user, request);
        WebResponse<AddressResponse> webResponse = new WebResponse<>();
        webResponse.setData(response);
        return webResponse;
    }
}
