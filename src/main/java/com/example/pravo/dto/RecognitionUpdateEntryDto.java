package com.example.pravo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecognitionUpdateEntryDto {
    private Integer points;
    private String status;
    private Boolean refereeApproval;
    private String admin;
    private Boolean adminApproval;
    private String modifiedBy;
}
