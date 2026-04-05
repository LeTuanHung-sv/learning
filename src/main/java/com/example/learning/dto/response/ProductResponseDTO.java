package com.example.learning.dto.response;

import com.example.learning.enums.ProductStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductResponseDTO {
  private UUID productId;
  @NotBlank
  private String productName;
  private BigDecimal price;
  @Enumerated(EnumType.STRING)
  private ProductStatus productStatus;
}
