package com.example.pravo.dto;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
public class CategoryDto {
    private Long id;
    private String name;
    private CreatedModifiedByDto createdBy;
    private LocalDateTime createdDate;
    private CreatedModifiedByDto modifiedBy;
    private LocalDateTime modifiedDate;
}
