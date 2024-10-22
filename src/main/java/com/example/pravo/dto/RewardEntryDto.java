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
    private Integer points;
    private boolean limited;
    private String limitedTime;
    private String image;
    private String createdBy;
    private String modifiedBy;
}
