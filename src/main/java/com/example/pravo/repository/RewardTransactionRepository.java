package com.example.pravo.repository;

import com.example.pravo.models.RewardTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RewardTransactionRepository extends JpaRepository<RewardTransaction, Long>, JpaSpecificationExecutor<RewardTransaction> {
}
