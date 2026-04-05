package com.example.learning.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends VersionedEntity {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID userId;
    @NotBlank
    private String userName;
    @NotBlank
    private String phone;

}
