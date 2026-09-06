package com.metrazh.agency.repository;

import com.metrazh.agency.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminUserRepository extends JpaRepository<AdminUser, Integer> {

    Optional<AdminUser> findByUsername(String username);
}
