package com.example.learning.service;

import com.example.learning.dto.request.InvoiceRequestDTO;
import com.example.learning.dto.response.InvoiceResponseDTO;

public interface InvoiceService {
  public InvoiceResponseDTO createInvoice(InvoiceRequestDTO invoiceRequestDTO);
}
