package com.example.learning.service.implement;

import com.example.learning.dto.request.InvoiceRequestDTO;
import com.example.learning.dto.response.InvoiceResponseDTO;
import com.example.learning.entity.Invoice;
import com.example.learning.mapper.InvoiceMapper;
import com.example.learning.repository.InvoiceRepository;
import com.example.learning.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
  private final InvoiceRepository invoiceRepository;
  private final InvoiceMapper invoiceMapper;

  @Override
  public InvoiceResponseDTO createInvoice(InvoiceRequestDTO invoiceRequestDTO){
    Invoice invoice = invoiceMapper.toEntity(invoiceRequestDTO);

    Invoice saved = invoiceRepository.save(invoice);
    return invoiceMapper.toResponse(saved);
  }
}
