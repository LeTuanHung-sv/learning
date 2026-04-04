package com.example.learning.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Builder
public class User extends VersionedEntity {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID userId;
    @NotBlank
    private String userName;
    @NotBlank
    private String phone;

}
