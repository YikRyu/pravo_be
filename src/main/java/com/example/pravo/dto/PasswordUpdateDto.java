package com.example.pravo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordUpdateDto {
    private String oldPassword;
    private String newPassword;
}
