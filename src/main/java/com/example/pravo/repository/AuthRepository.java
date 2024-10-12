package com.example.pravo.repository;

import com.example.pravo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AuthRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
}
