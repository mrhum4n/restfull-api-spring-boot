package com.project.restfull.api.service.impl;

import com.project.restfull.api.model.Address;
import com.project.restfull.api.model.Contact;
import com.project.restfull.api.model.User;
import com.project.restfull.api.pojo.AddressResponse;
import com.project.restfull.api.pojo.CreateAddressRequest;
import com.project.restfull.api.pojo.UpdateAddressRequest;
import com.project.restfull.api.repository.AddressRepo;
import com.project.restfull.api.repository.ContactRepo;
import com.project.restfull.api.service.AddressService;
import com.project.restfull.api.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private ContactRepo contactRepo;
    @Autowired
    private AddressRepo addressRepo;
    @Autowired
    private ValidationService validationService;

    @Override
    @Transactional
    public AddressResponse create(User user, CreateAddressRequest request) {
        validationService.validation(request);

        Contact contact = contactRepo.findFirstByUserAndId(user.getId(),request.getContactId());
        if (contact == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found");
        }

        Address address = new Address();
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setProvince(request.getProvince());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());
        address.setContact(contact);

        addressRepo.save(address);

        return toAddressResponse(address);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse getById(User user, String contactId, String addressId) {
        Contact contact = contactRepo.findFirstByUserAndId(user.getId(),contactId);
        if (contact == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found");
        }

        Address address = addressRepo.findFirstByContactAndId(contactId, addressId);
        if (address == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found");
        }

        return toAddressResponse(address);
    }

    @Override
    @Transactional
    public AddressResponse update(User user, UpdateAddressRequest request) {
        validationService.validation(request);

        Contact contact = contactRepo.findFirstByUserAndId(user.getId(), request.getContactId());
        if (contact == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found");
        }

        Address address = addressRepo.findFirstByContactAndId(request.getContactId(), request.getAddressId());
        if (address == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found");
        }

        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setProvince(request.getProvince());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());

        addressRepo.save(address);

        return toAddressResponse(address);
    }

    private AddressResponse toAddressResponse(Address address) {
        AddressResponse response = new AddressResponse();
        response.setId(address.getId());
        response.setStreet(address.getStreet());
        response.setCity(address.getCity());
        response.setProvince(address.getProvince());
        response.setCountry(address.getCountry());
        response.setPostalCode(address.getPostalCode());
        return response;
    }
}
