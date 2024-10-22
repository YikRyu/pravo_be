package com.example.pravo.services;

import com.example.pravo.dto.ChartRewardsDto;
import com.example.pravo.dto.RewardEntryDto;
import com.example.pravo.mapper.MapStructMapper;
import com.example.pravo.models.Category;
import com.example.pravo.models.Reward;
import com.example.pravo.models.User;
import com.example.pravo.repository.AuthRepository;
import com.example.pravo.repository.CategoryRepository;
import com.example.pravo.repository.RewardRepository;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecificationConverterImpl;
import com.turkraft.springfilter.parser.node.FilterNode;
import jakarta.transaction.Transactional;
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
public class RewardService {
    @Autowired
    private RewardRepository rewardRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private FilterBuilder fb;
    @Autowired
    private MapStructMapper mapper;
    @Autowired
    private FilterSpecificationConverterImpl filterService;

    private Specification<Reward> specificationConverter(FilterNode filterNode){
        return filterService.convert(filterNode);
    }

    private User getUser(String userId){
        User user = authRepository.findById(userId).orElse(null);
        if (user == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist!");

        return user;
    }

    private Category getCategory(Long categoryId){
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category does not exist!");
        return category;
    }

    public Page<Reward> getRewards(Pageable pageable) {
        return rewardRepository.findAll(pageable);
    }

    @Transactional
    public Page<Reward> getActiveRewards(Pageable pageable) {
        return rewardRepository.findByActiveTrue(pageable);
    }

    public List<ChartRewardsDto> getChartRewards(List<Long> rewardIds){
        FilterNode rewardFilterNode = fb.field("id").in(fb.input(rewardIds)).get();
        List<Reward> rewards = rewardRepository.findAll(specificationConverter(rewardFilterNode));
        return rewards.stream().map(reward -> mapper.toChartRewardsDto(reward)).toList();
    }

    public List<Reward> getRewardsDetails(List<Long> rewardIds){
        FilterNode rewardsFilterNode = fb.field("id").in(fb.input(rewardIds)).get();
        return rewardRepository.findAll(specificationConverter(rewardsFilterNode));
    }

    @Transactional
    public Reward postReward (RewardEntryDto reward) {
        FilterNode duplicateRewardFilterNode = fb.field("name").equal(fb.input(reward.getName().trim())).and(fb.field("active").equal(fb.input(true))).get();
        List<Reward> duplicateReward = rewardRepository.findAll(specificationConverter(duplicateRewardFilterNode));
        if (!duplicateReward.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reward with a same name has already existed!");

        Reward newReward = new Reward();
        newReward.setName(reward.getName().trim());
        newReward.setDescription(reward.getDescription().trim());
        newReward.setCategory(getCategory(reward.getCategory()));
        newReward.setPoints(reward.getPoints());
        newReward.setLimited(reward.isLimited());
        if(reward.getLimitedTime() != null) newReward.setLimitedTime(LocalDateTime.parse(reward.getLimitedTime()));
        else newReward.setLimitedTime(null);
        newReward.setImage(reward.getImage());
        newReward.setCreatedBy(getUser(reward.getCreatedBy()));
        newReward.setCreatedDate(LocalDateTime.now());
        newReward.setActive(true);

        return  rewardRepository.save(newReward);
    }

    @Transactional
    public Reward putReward (RewardEntryDto reward, Long rewardId) {
        Reward oldReward = rewardRepository.findById(rewardId).orElse(null);
        if (oldReward == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reward does not exist!");

        List<Reward> duplicateReward = rewardRepository.findByNameAndIdNotAndActiveTrue(reward.getName().trim(), rewardId);
        if (!duplicateReward.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reward with a same name has already existed!");

        oldReward.setName(reward.getName().trim());
        oldReward.setDescription(reward.getDescription().trim());
        oldReward.setCategory(getCategory(reward.getCategory()));
        oldReward.setPoints(reward.getPoints());
        oldReward.setLimited(reward.isLimited());
        if(reward.getLimitedTime() != null) oldReward.setLimitedTime(LocalDateTime.parse(reward.getLimitedTime()));
        else oldReward.setLimitedTime(null);
        oldReward.setImage(reward.getImage());
        oldReward.setModifiedBy(getUser(reward.getModifiedBy()));
        oldReward.setModifiedDate(LocalDateTime.now());

        return  rewardRepository.save(oldReward);
    }

    public Reward updateQuantity (Long rewardId, Integer quantity){
        Reward oldReward = rewardRepository.findById(rewardId).orElse(null);
        if (oldReward == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reward does not exist!");

        oldReward.setQuantity(quantity);

        return rewardRepository.save(oldReward);
    }

    public boolean deleteReward (Long rewardId) {
        FilterNode rewardFilterNode = fb.field("id").equal(fb.input(rewardId)).get();
        Reward reward = rewardRepository.findOne(specificationConverter(rewardFilterNode)).orElse(null);
        if (reward == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reward does not exist!");
        else{
            reward.setQuantity(0);
            reward.setActive(false);
            rewardRepository.save(reward);

            return true;
        }

    }
}
