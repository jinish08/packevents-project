package com.jinish.ncsu.sad.userservice.repository;

import com.jinish.ncsu.sad.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // Spring Data JPA will automatically create methods based on their names.

     Optional<User> findByEmail(String email);
}