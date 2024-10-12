package com.example.pravo.repository;

import com.example.pravo.models.Recognition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RecognitionRepository extends JpaRepository<Recognition, Long>, JpaSpecificationExecutor<Recognition> {
}
