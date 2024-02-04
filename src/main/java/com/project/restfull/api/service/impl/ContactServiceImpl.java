package com.project.restfull.api.service.impl;

import com.project.restfull.api.model.Contact;
import com.project.restfull.api.model.User;
import com.project.restfull.api.pojo.ContactResponse;
import com.project.restfull.api.pojo.CreateContactRequest;
import com.project.restfull.api.pojo.SearchContactRequest;
import com.project.restfull.api.pojo.UpdateContactRequest;
import com.project.restfull.api.repository.ContactRepo;
import com.project.restfull.api.service.ContactService;
import com.project.restfull.api.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    private ContactRepo contactRepo;
    @Autowired
    private ValidationService validationService;

    @Override
    @Transactional
    public ContactResponse createContact(User user, CreateContactRequest request) {
        validationService.validation(request);

        Contact contact = new Contact();
        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setPhone(request.getPhone());
        contact.setEmail(request.getEmail());
        contact.setUser(user);
        contactRepo.save(contact);

        ContactResponse response = new ContactResponse();
        response.setId(contact.getId());
        response.setFirstName(contact.getFirstName());
        response.setLastName(contact.getLastName());
        response.setPhone(contact.getPhone());
        response.setEmail(contact.getEmail());

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public ContactResponse getContact(User user, String id) {
        Contact contact = contactRepo.findFirstByUserAndId(user.getId(), id);
        if (contact == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found");
        }

        ContactResponse response = new ContactResponse();
        response.setId(contact.getId());
        response.setFirstName(contact.getFirstName());
        response.setLastName(contact.getLastName());
        response.setPhone(contact.getPhone());
        response.setEmail(contact.getEmail());

        return response;
    }

    @Override
    @Transactional
    public ContactResponse updateContact(User user, UpdateContactRequest request) {
        validationService.validation(request);

        Contact contact = contactRepo.findFirstByUserAndId(user.getId(), request.getId());
        if (contact == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found");
        }

        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contactRepo.save(contact);

        ContactResponse response = new ContactResponse();
        response.setId(contact.getId());
        response.setFirstName(contact.getFirstName());
        response.setLastName(contact.getLastName());
        response.setPhone(contact.getPhone());
        response.setEmail(contact.getEmail());

        return response;
    }

    @Override
    @Transactional
    public void deleteContact(User user, String id) {
        Contact contact = contactRepo.findFirstByUserAndId(user.getId(), id);
        if (contact == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found");
        }
        contactRepo.delete(contact);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactResponse> searchContact(User user, SearchContactRequest request) {
        Specification<Contact> specification = (root, query, builder) -> {
            // MEMBUAT PREDICATE UNTUK FILTER QUERY SESUAI DENGAN ISI REQUEST
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("user"), user));
            if (Objects.nonNull(request.getName())) {
                predicates.add(builder.or(
                        builder.like(root.get("firstName"), "%"+ request.getName() +"%"),
                        builder.like(root.get("lastName"), "%"+ request.getName() +"%")
                ));
            }
            if (Objects.nonNull(request.getEmail())) {
                predicates.add(builder.like(root.get("email"), "%"+ request.getEmail() +"%"));
            }
            if (Objects.nonNull(request.getPhone())) {
                predicates.add(builder.like(root.get("phone"), "%"+ request.getPhone() +"%"));
            }
            // MEMBUAT PREDICATE UNTUK FILTER QUERY SESUAI DENGAN ISI REQUEST
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Contact> contacts = contactRepo.findAll(specification, pageable);
        List<ContactResponse> responses = contacts.getContent().stream().map(contact -> {
            ContactResponse contactResponse = new ContactResponse();
            contactResponse.setId(contact.getId());
            contactResponse.setFirstName(contact.getFirstName());
            contactResponse.setLastName(contact.getLastName());
            contactResponse.setEmail(contact.getEmail());
            contactResponse.setPhone(contact.getPhone());
            return contactResponse;
        }).collect(Collectors.toList());

        return new PageImpl<>(responses, pageable, contacts.getTotalElements());
    }
}
