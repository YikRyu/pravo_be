package com.example.pravo.controller;

import com.example.pravo.dto.ChartRewardTransactionDto;
import com.example.pravo.dto.CreatedModifiedByDto;
import com.example.pravo.dto.RewardTransactionDto;
import com.example.pravo.dto.RewardTransactionEntryDto;
import com.example.pravo.mapper.MapStructMapper;
import com.example.pravo.models.RewardTransaction;
import com.example.pravo.models.User;
import com.example.pravo.services.RewardTransactionServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/")
public class RewardTransactionController {
    @Autowired
    private RewardTransactionServices rewardTransactionServices;
    @Autowired
    private MapStructMapper mapper;

    private CreatedModifiedByDto mapCreatedModifiedBy(User user){
        return mapper.toCreatedModifiedByDto(user);
    }

    private RewardTransactionDto mapTransaction (RewardTransaction transaction){
        RewardTransactionDto mappedTransaction = new RewardTransactionDto();

        mappedTransaction.setId(transaction.getId());
        mappedTransaction.setPoints(transaction.getPoints());
        mappedTransaction.setUserId(mapCreatedModifiedBy(transaction.getUserId()));
        mappedTransaction.setCreatedDate(transaction.getCreatedDate());
        mappedTransaction.setRewards(transaction.getRewards());

        return mappedTransaction;
    }

    @GetMapping(path = "/pointsTransactions/{userId}")
    public ResponseEntity<Map<String, Object>> getTransactions(
            @PathVariable(value = "userId") String userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        int totalItems = 0;
        Map<String, Object> response = new HashMap<>();
        Pageable pageable = PageRequest.of(page, size);
        Page<RewardTransaction> pointsTransactions = rewardTransactionServices.getRewardsTransactions(userId, pageable);

        List<RewardTransaction> data = pointsTransactions.getContent();
        long numberOfElements = pointsTransactions.getTotalElements();

        //convert total number of items as int if it doesnt reach long amount worth of amount...
        if (numberOfElements < Integer.MAX_VALUE) {
            totalItems = (int) numberOfElements;
            response.put("totalItems", totalItems);
        } else {
            response.put("totalItems", numberOfElements);
        }

        if(!data.isEmpty()){
            for (RewardTransaction transaction: data){
                mapTransaction(transaction);
            }
        }
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/pointsTransactions/chart")
    public List<ChartRewardTransactionDto> getChartRewardTransactions(){
        return rewardTransactionServices.getChartRewardTransactions();
    }

    @PostMapping(path = "/pointsTransactions")
    public RewardTransaction postTransaction(
            @RequestBody RewardTransactionEntryDto transaction
    ) {
        return rewardTransactionServices.postPointsTransaction(transaction);
    }
}
