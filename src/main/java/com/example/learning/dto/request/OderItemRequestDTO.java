package com.example.learning.dto.request;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OderItemRequestDTO {
  private UUID productId;
  @Positive
  private BigDecimal quantity;
}
