package com.example.learning.entity;

import com.example.learning.enums.ProductStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
public class Product extends VersionedEntity {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID productId;
    @NotBlank
    private String productName;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

}
