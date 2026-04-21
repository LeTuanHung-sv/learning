package com.example.learning.service;

import com.example.learning.dto.request.InvoiceRequestDTO;
import com.example.learning.dto.response.InvoiceResponseDTO;
import java.util.UUID;

public interface InvoiceService {
  public InvoiceResponseDTO createInvoice(InvoiceRequestDTO invoiceRequestDTO);
  public InvoiceResponseDTO getInvoiceId(UUID id);
}
