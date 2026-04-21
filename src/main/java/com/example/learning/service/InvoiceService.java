package com.example.learning.service;

import com.example.learning.dto.request.InvoiceRequestDTO;
import com.example.learning.dto.response.InvoiceResponseDTO;
import java.util.List;

public interface InvoiceService {
  public InvoiceResponseDTO createInvoice(InvoiceRequestDTO invoiceRequestDTO);
  public List<InvoiceResponseDTO> getAllInvoice();
}
