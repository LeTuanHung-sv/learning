package com.example.learning.dto.request;

import com.example.learning.dto.response.OderItemResponse;
import com.example.learning.enums.OderStatus;
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
  private UUID userId;
  private BigDecimal totalAmount;
  private OderStatus oderStatus;
  private List<OderItemRequestDTO> items;
}
