package com.example.pravo.dto;

import com.example.pravo.models.Recognition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointTransactionDto {
    private Long id;
    private CreatedModifiedByDto userId;
    private Recognition recognitionId;
    private LocalDateTime createdDate;
}
