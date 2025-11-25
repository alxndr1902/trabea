package com.bc46.trabea.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.workEmail = :workEmail")
    Optional<User> findByWorkEmail(String workEmail);
}
