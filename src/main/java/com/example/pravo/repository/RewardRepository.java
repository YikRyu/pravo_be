package com.example.pravo.repository;

import com.example.pravo.models.Reward;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RewardRepository extends JpaRepository<Reward, Long>, JpaSpecificationExecutor<Reward> {
    public Page<Reward> findByActiveTrue(Pageable pageable);
    public List<Reward> findByNameAndIdNotAndActiveTrue(String name, Long rewardId);
    public List<Reward> findByNameAndActiveTrue(String name);
}
