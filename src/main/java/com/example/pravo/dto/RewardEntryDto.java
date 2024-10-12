package com.example.pravo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardEntryDto {
    private String name;
    private String description;
    private Long category;
    private boolean limited;
    private Integer limitedAmount;
    private String limitedTime;
    private String createdBy;
    private String modifiedBy;
}
