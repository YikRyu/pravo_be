package com.example.pravo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String id;
    private String email;
    private String name;
    private String type;
    private Integer points;
    private String position;
    private String department;
    private String contact;
    private String address;

    private boolean active;
}
