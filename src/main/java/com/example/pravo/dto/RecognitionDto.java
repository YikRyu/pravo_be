package com.example.pravo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecognitionDto {
    private Long id;
    private String type;
    private String title;
    private String description;
    private Integer points;
    private String status;
    private CreatedModifiedByDto referee;
    private Boolean refereeApproval;
    private CreatedModifiedByDto admin;
    private Boolean adminApproval;
    private CreatedModifiedByDto peer;
    private CreatedModifiedByDto createdBy;
    private LocalDateTime createdDate;
    private CreatedModifiedByDto modifiedBy;
    private LocalDateTime modifiedDate;
}
