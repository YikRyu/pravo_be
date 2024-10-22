package com.example.pravo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@Table(name = "users")
public class User {
    @Id
    @NotBlank
    String id;

    @Length(max = 255)
    @Column
    String name;

    @Length(max = 255)
    @Column
    String email;

    @Length(max = 255)
    @Column
    String password;

    @Length(max = 5)
    @Column
    String type;

    @Column
    Integer points;

    @Column
    String position;

    @Column
    String department;

    @Length(max = 255)
    @Column
    String contact;

    @Length(max = 255)
    @Column
    String address;

    @Column
    boolean active;
}
