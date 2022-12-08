package com.bovetlc.user_authentication_service.repository;

import com.bovetlc.user_authentication_service.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUsername(String username);
    Optional<Admin> findByEmail(String email);
}
