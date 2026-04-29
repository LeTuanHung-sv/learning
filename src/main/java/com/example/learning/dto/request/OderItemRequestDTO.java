package com.example.learning.dto.request;

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
  private BigDecimal quantity;
}
