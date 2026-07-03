package com.example.learning.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;
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
    @NotNull
    private String fullName;
    @NotNull
    @Size(max=15)
    private String phone;
    @NotNull
    private String address;
}
