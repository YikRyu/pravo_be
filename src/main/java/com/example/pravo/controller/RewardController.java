package com.example.pravo.controller;

import com.example.pravo.dto.ChartRewardsDto;
import com.example.pravo.dto.RewardEntryDto;
import com.example.pravo.dto.RewardQuantityBulkEntryDto;
import com.example.pravo.mapper.MapStructMapper;
import com.example.pravo.models.Reward;
import com.example.pravo.services.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/")
public class RewardController {
    @Autowired
    private RewardService rewardService;
    @Autowired
    private MapStructMapper mapper;

    @GetMapping(path = "/reward")
    public ResponseEntity<Map<String, Object>> getRewards(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        int totalItems = 0;
        Map<String, Object> response = new HashMap<>();
        Pageable pageable = PageRequest.of(page, size);
        Page<Reward> rewards = rewardService.getRewards(pageable);

        List<Reward> data = rewards.getContent();
        long numberOfElements = rewards.getTotalElements();

        //convert total number of items as int if it doesnt reach long amount worth of amount...
        if (numberOfElements < Integer.MAX_VALUE) {
            totalItems = (int) numberOfElements;
            response.put("totalItems", totalItems);
        } else {
            response.put("totalItems", numberOfElements);
        }
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/reward/active")
    public ResponseEntity<Map<String, Object>> getActiveRewards (
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ){
        int totalItems = 0;
        Map<String, Object> response = new HashMap<>();
        Pageable pageable = PageRequest.of(page, size);
        Page<Reward> rewards = rewardService.getActiveRewards(pageable);

        List<Reward> data = rewards.getContent();
        long numberOfElements = rewards.getTotalElements();

        //convert total number of items as int if it doesnt reach long amount worth of amount...
        if (numberOfElements < Integer.MAX_VALUE) {
            totalItems = (int) numberOfElements;
            response.put("totalItems", totalItems);
        } else {
            response.put("totalItems", numberOfElements);
        }
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/reward/details")
    public List<Reward> getRewardsDetails(
            @RequestParam(value = "rewardId") List<Long> rewardIds
    ) {
        return rewardService.getRewardsDetails(rewardIds);
    }

    @PostMapping(path = "/reward/chart")
    public List<ChartRewardsDto> getChartRewards(
            @RequestBody List<Long> rewardIds
    ){
        return rewardService.getChartRewards(rewardIds);
    }

    @PostMapping(path = "/reward")
    public Reward postReward(
            @RequestBody RewardEntryDto newReward
    ) throws SQLException {
        return rewardService.postReward(newReward);
    }

    @PutMapping(path = "/reward/{rewardId}")
    public Reward putReward(
            @RequestBody RewardEntryDto reward,
            @PathVariable(value = "rewardId") Long rewardId
    ) throws SQLException {
        return rewardService.putReward(reward, rewardId);
    }

    @PutMapping(path = "/reward/quantity/bulk")
    public List<Reward> bulkUpdateAmount(
            @RequestBody List<RewardQuantityBulkEntryDto> bulkReward
    ){
        return rewardService.bulkUpdateQuantity(bulkReward);
    }

    @PutMapping(path = "/reward/quantity/{rewardId}")
    public Reward updateAmount(
            @PathVariable(value = "rewardId") Long rewardId,
            @RequestParam(value = "quantity") int quantity
    ){
        return rewardService.updateQuantity(rewardId, quantity);
    }

    @DeleteMapping(path = "/reward/{rewardId}")
    public boolean deleteReward(
            @PathVariable(value = "rewardId") Long rewardId
    ){
        return rewardService.deleteReward(rewardId);
    }
}
