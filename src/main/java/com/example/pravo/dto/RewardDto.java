package com.example.pravo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RewardDto {
    private Long id;
    private String name;
    private String description;
    private CategoryDto category;
    private Integer points;
    private Integer quantity;
    private boolean limited;
    private LocalDateTime limitedTime;
    private CreatedModifiedByDto createdBy;
    private LocalDateTime createdDate;
    private CreatedModifiedByDto modifiedBy;
    private LocalDateTime modifiedDate;
}
