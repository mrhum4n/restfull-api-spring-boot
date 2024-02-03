package com.project.restfull.api.repository;

import com.project.restfull.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, String> {
    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findUserByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.token = :token")
    User findUserByToken(String token);
}
