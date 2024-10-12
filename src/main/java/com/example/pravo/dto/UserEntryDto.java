package com.example.pravo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntryDto {
    private String id;
    private String email;
    private String password;
    private String name;
    private String type;
    private String position;
    private String department;
    private String contact;
    private String address;
}
