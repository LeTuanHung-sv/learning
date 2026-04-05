package com.example.learning.dto.request;

import com.example.learning.enums.ProductStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequestDTO {
  @NotBlank
  private String productName;
  private BigDecimal price;
  @Enumerated(EnumType.STRING)
  private ProductStatus productStatus;
}
