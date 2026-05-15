package com.example.learning.dto.response;

import com.example.learning.dto.request.OderItemRequestDTO;
import com.example.learning.entity.OderItem;
import com.example.learning.enums.InvoiceStatus;
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
public class OderResponseDTO {
  private UUID orderId;
  private UUID invoiceId;
  private BigDecimal totalAmount;
  private OderStatus orderStatus;
  private InvoiceStatus invoiceStatus;
}
