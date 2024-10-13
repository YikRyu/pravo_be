package com.example.pravo.repository;

import com.example.pravo.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    public Page<User> findByType(String type,Pageable pageable);
}
