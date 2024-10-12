package com.example.pravo.services;

import com.example.pravo.dto.ChartRewardTransactionDto;
import com.example.pravo.dto.RewardTransactionEntryDto;
import com.example.pravo.mapper.MapStructMapper;
import com.example.pravo.models.RewardTransaction;
import com.example.pravo.models.Recognition;
import com.example.pravo.models.User;
import com.example.pravo.repository.AuthRepository;
import com.example.pravo.repository.RewardTransactionRepository;
import com.example.pravo.repository.RecognitionRepository;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecificationConverterImpl;
import com.turkraft.springfilter.parser.node.FilterNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class RewardTransactionServices {
    @Autowired
    private RewardTransactionRepository rewardTransactionRepository;
    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private RecognitionRepository recognitionRepository;
    @Autowired
    private FilterBuilder fb;
    @Autowired
    private MapStructMapper mapper;
    @Autowired
    private FilterSpecificationConverterImpl filterService;

    private Specification<RewardTransaction> specificationConverter(FilterNode filterNode){
        return filterService.convert(filterNode);
    }

    public Page<RewardTransaction> getRewardsTransactions(String userId, Pageable pageable){
        FilterNode filterNode = fb.field("user_id").equal(fb.input(userId)).get();
        return rewardTransactionRepository.findAll(specificationConverter(filterNode), pageable);
    }

    public List<ChartRewardTransactionDto> getChartRewardTransactions(){
        List<RewardTransaction> transactions = rewardTransactionRepository.findAll();
        return transactions.stream().map(transaction -> mapper.toChartRewardTransactionDto(transaction)).toList();
    }

    public RewardTransaction postPointsTransaction(RewardTransactionEntryDto transaction){
        RewardTransaction rewardTransaction = new RewardTransaction();

        User userId = authRepository.findById(transaction.getUserId()).orElse(null);
        if (userId == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist!");

        rewardTransaction.setPoints(transaction.getPoints());
        rewardTransaction.setRewards(transaction.getRewards());
        rewardTransaction.setUserId(userId);
        rewardTransaction.setCreatedDate(LocalDateTime.now());

        return rewardTransactionRepository.save(rewardTransaction);
    }
}
