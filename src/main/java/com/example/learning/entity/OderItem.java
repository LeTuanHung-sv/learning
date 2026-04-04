package com.example.learning.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class OderItem extends VersionedEntity{
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID OderItemId;
    private UUID oderId;
    private UUID productId;
    private BigDecimal quantity;
    private BigDecimal price;

}
