package com.example.pravo.repository;

import com.example.pravo.models.Recognition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RecognitionRepository extends JpaRepository<Recognition, Long>, JpaSpecificationExecutor<Recognition> {
    List<Recognition> findByIdIn(List<Long> recognitionIds);
}
