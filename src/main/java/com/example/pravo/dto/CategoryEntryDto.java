package com.example.pravo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntryDto {
    private String name;
    private String createdBy;
    private String modifiedBy;
}
