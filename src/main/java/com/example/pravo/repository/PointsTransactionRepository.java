package com.example.pravo.repository;

import com.example.pravo.models.PointsTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PointsTransactionRepository extends JpaRepository<PointsTransaction, Long>, JpaSpecificationExecutor<PointsTransaction> {
}
