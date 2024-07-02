package com.ecomerce.auth.repository;

import com.ecomerce.auth.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);

}
