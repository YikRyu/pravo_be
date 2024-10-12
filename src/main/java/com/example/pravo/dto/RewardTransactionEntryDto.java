package com.example.pravo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardTransactionEntryDto {
    private Integer points;
    private Long recognitionId;
    private String rewards;
    private String userId;
}
