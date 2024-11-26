package com.example.pravo.controller;

import com.example.pravo.dto.*;
import com.example.pravo.mapper.MapStructMapper;
import com.example.pravo.models.Category;
import com.example.pravo.models.Reward;
import com.example.pravo.models.User;
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

    private CreatedModifiedByDto mapCreatedModifiedBy(User user){
        return mapper.toCreatedModifiedByDto(user);
    }

    private RewardDto mapReward (Reward reward){
        RewardDto mappedReward = new RewardDto();

        mappedReward.setId(reward.getId());
        mappedReward.setName(reward.getName());
        mappedReward.setDescription(reward.getDescription());
        mappedReward.setCategory(mapCategory(reward.getCategory()));
        mappedReward.setPoints(reward.getPoints());
        mappedReward.setQuantity(reward.getQuantity());
        mappedReward.setLimited(reward.isLimited());
        if(reward.getLimitedTime() != null) mappedReward.setLimitedTime(reward.getLimitedTime());
        else mappedReward.setLimitedTime(null);
        mappedReward.setCreatedBy(mapCreatedModifiedBy(reward.getCreatedBy()));
        mappedReward.setCreatedDate(reward.getCreatedDate());
        if (reward.getModifiedBy() != null) mappedReward.setModifiedBy(mapCreatedModifiedBy(reward.getModifiedBy()));
        mappedReward.setModifiedDate(reward.getModifiedDate());

        return mappedReward;
    }

    private CategoryDto mapCategory (Category category){
        CategoryDto mappedCategory = new CategoryDto();

        mappedCategory.setId(category.getId());
        mappedCategory.setName(category.getName());
        mappedCategory.setCreatedBy(mapCreatedModifiedBy(category.getCreatedBy()));
        mappedCategory.setCreatedDate(category.getCreatedDate());
        if (category.getModifiedBy() != null) mappedCategory.setModifiedBy(mapCreatedModifiedBy(category.getModifiedBy()));
        else mappedCategory.setModifiedBy(null);
        mappedCategory.setModifiedDate(category.getModifiedDate());

        return mappedCategory;
    }

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

        if(!data.isEmpty()){
            for(Reward reward: data){
                mapReward(reward);
            }
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

        if(!data.isEmpty()){
            for(Reward reward: data){
                mapReward(reward);
            }
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
        List<ChartRewardsDto> data = rewardService.getChartRewards(rewardIds);

        if(!data.isEmpty()){
            for (ChartRewardsDto chartReward: data){
                mapCategory(chartReward.getCategory());
            }
        }
        return data;
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
