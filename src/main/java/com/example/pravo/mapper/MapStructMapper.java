package com.example.pravo.mapper;

import com.example.pravo.dto.*;
import com.example.pravo.models.RewardTransaction;
import com.example.pravo.models.Reward;
import com.example.pravo.models.User;
import org.mapstruct.*;

@Mapper(componentModel ="spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MapStructMapper {
    User toUser(LoginDto loginDto);
    UserDto toUserDto(User user);
    ChartRewardTransactionDto toChartRewardTransactionDto(RewardTransaction transaction);
    ChartRewardsDto toChartRewardsDto(Reward reward);
    CreatedModifiedByDto toCreatedModifiedByDto(User user);
}
