package com.project.restfull.api.service;

import com.project.restfull.api.model.User;
import com.project.restfull.api.pojo.AddressResponse;
import com.project.restfull.api.pojo.CreateAddressRequest;

public interface AddressService {
    AddressResponse create(User user, CreateAddressRequest request);
}
