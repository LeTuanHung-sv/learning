package com.example.learning.entity;

import com.example.learning.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Oder extends VersionedEntity{
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID oderId;
    private UUID userId;
    @Enumerated(EnumType.STRING)
    private OrderStatus oderStatus;
    @NotNull
    private BigDecimal totalAmount;
    private String shippingAddress;


}
