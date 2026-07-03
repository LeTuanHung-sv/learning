package com.example.learning.dto.request;

import com.example.learning.enums.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OderRequestDTO {
  @NotBlank
  private UUID userId;
  @Positive
  private BigDecimal totalAmount;
  private OrderStatus oderStatus;
  private List<OderItemRequestDTO> items;


}
