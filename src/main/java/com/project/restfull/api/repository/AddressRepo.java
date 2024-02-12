package com.project.restfull.api.repository;

import com.project.restfull.api.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepo extends JpaRepository<Address, String> {
    @Query("SELECT a FROM Address a JOIN Contact ON a.contact.id = :contactId WHERE a.id = :addressId")
    Address findFirstByContactAndId(String contactId, String addressId);

    @Query("SELECT a FROM Address a JOIN Contact ON a.contact.id = :contactId")
    List<Address> findAllByContact(String contactId);
}
