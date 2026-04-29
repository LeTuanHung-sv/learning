package com.example.learning.dto.response;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OderItemResponse {
  private UUID productId;
  private BigDecimal quantity;
  private BigDecimal price;
}
