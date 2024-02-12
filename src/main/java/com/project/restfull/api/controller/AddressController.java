package com.project.restfull.api.controller;

import com.project.restfull.api.model.User;
import com.project.restfull.api.pojo.AddressResponse;
import com.project.restfull.api.pojo.CreateAddressRequest;
import com.project.restfull.api.pojo.UpdateAddressRequest;
import com.project.restfull.api.pojo.WebResponse;
import com.project.restfull.api.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @PostMapping(path = "/contact/{contactId}/addresses", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<AddressResponse> create(User user, @RequestBody CreateAddressRequest request, @PathVariable("contactId") String contactId) {
        request.setContactId(contactId);
        AddressResponse response = addressService.create(user, request);
        WebResponse<AddressResponse> webResponse = new WebResponse<>();
        webResponse.setData(response);
        return webResponse;
    }

    @GetMapping(path = "/contact/{contactId}/addresses/{addressId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<AddressResponse> getById(User user, @PathVariable("contactId") String contactId, @PathVariable("addressId") String addressId) {
        AddressResponse response = addressService.getById(user, contactId, addressId);
        WebResponse<AddressResponse> webResponse = new WebResponse<>();
        webResponse.setData(response);
        return webResponse;
    }

    @PutMapping(path = "/contact/{contactId}/addresses/{addressId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<AddressResponse> update(User user, @RequestBody UpdateAddressRequest request, @PathVariable("contactId") String contactId, @PathVariable("addressId") String addressId) {
        request.setContactId(contactId);
        request.setAddressId(addressId);
        AddressResponse response = addressService.update(user, request);
        WebResponse<AddressResponse> webResponse = new WebResponse<>();
        webResponse.setData(response);
        return webResponse;
    }

    @DeleteMapping(path = "/contact/{contact_id}/addresses/{address_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> delete(User user, @PathVariable("contact_id") String contactId, @PathVariable("address_id") String addressId) {
        addressService.delete(user, contactId, addressId);
        WebResponse<String> webResponse = new WebResponse<>();
        webResponse.setData("Ok");
        return webResponse;
    }

    @GetMapping(path = "/contact/{contact_id}/addresses", produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<List<AddressResponse>> getList(User user, @PathVariable("contact_id") String contactId) {
        List<AddressResponse> response = addressService.findAll(user, contactId);
        WebResponse<List<AddressResponse>> webResponse = new WebResponse<>();
        webResponse.setData(response);
        return webResponse;
    }
}
