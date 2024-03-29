package com.project.restfull.api.service;

import com.project.restfull.api.model.User;
import com.project.restfull.api.pojo.AddressResponse;
import com.project.restfull.api.pojo.CreateAddressRequest;
import com.project.restfull.api.pojo.UpdateAddressRequest;

import java.util.List;

public interface AddressService {
    AddressResponse create(User user, CreateAddressRequest request);
    AddressResponse getById(User user, String contactId, String addressId);
    AddressResponse update(User user, UpdateAddressRequest request);
    void delete(User user, String contactId, String addressId);
    List<AddressResponse> findAll(User user, String contactId);
}
