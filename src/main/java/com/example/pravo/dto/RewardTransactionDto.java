package com.example.pravo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RewardTransactionDto {
    private Long id;
    private Integer points;
    private CreatedModifiedByDto userId;
    private LocalDateTime createdDate;
    private String rewards;
}
