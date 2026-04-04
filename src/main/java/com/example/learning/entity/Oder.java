package com.example.learning.entity;

import com.example.learning.enums.OderStatus;
import jakarta.persistence.*;
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
    private BigDecimal totalAmount;
    private OderStatus oderStatus;

}
