package com.project.restfull.api.repository;

import com.project.restfull.api.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepo extends JpaRepository<Address, String> {
}
