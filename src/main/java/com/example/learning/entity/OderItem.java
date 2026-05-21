package com.example.learning.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private UUID orderId;
    @NotNull
    private UUID productId;
    @NotNull
    private BigDecimal quantity;
    @NotNull
    private BigDecimal unitPrice;
    @NotNull
    private BigDecimal subtotal;


}
