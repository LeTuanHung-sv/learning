package com.example.learning.dto.response;

import com.example.learning.dto.request.OderItemRequestDTO;
import com.example.learning.enums.OrderStatus;
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
public class OderResponseDTO {
  private UUID oderId;
  private UUID userId;
  private BigDecimal totalAmount;
  private OrderStatus oderStatus;
  private List<OderItemResponse> items;
}
