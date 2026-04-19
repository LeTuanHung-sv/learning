package com.example.learning.dto.request;

import com.example.learning.enums.InvoiceStatus;
import com.example.learning.enums.PaymentMethod;
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
public class InvoiceRequestDTO {
  private UUID oderId;
  private BigDecimal invoiceNumber;
  private BigDecimal totalAmount;
  private PaymentMethod paymentMethod;
  private InvoiceStatus invoiceStatus;

}
