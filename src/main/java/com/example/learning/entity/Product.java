package com.example.learning.entity;

import com.example.learning.enums.ProductStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends VersionedEntity {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID productId;
    @NotBlank
    private String productName;
    @Max(100)
    private String description;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;
}
