package com.project.restfull.api.repository;

import com.project.restfull.api.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepo extends JpaRepository<Contact, String> {
}
