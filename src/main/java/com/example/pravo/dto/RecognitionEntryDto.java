package com.example.pravo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecognitionEntryDto {
    private String title;
    private String description;
    private String type;
    private String referee;
    private String peer;
    private String createdBy;
}
