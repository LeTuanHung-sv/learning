package com.example.learning.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.util.UUID;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Inventory extends VersionedEntity{
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID inventoryId;
    private UUID productId;
    @NotNull
    @PositiveOrZero
    private Integer quantity;

}
