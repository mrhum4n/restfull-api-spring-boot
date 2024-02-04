package com.project.restfull.api.repository;

import com.project.restfull.api.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ContactRepo extends JpaRepository<Contact, String>, JpaSpecificationExecutor<Contact> {
    @Query("SELECT c FROM Contact c JOIN User ON c.user.id = :userId WHERE c.id = :contactId")
    Contact findFirstByUserAndId(String userId, String contactId);
}
