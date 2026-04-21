package com.example.learning.controller;

import com.example.learning.dto.request.InvoiceRequestDTO;
import com.example.learning.dto.response.InvoiceResponseDTO;
import com.example.learning.service.InvoiceService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
public class InvoiceController {
  private final InvoiceService invoiceService;

  @PostMapping("/create")
  public ResponseEntity<InvoiceResponseDTO> createInvoice(@Valid @RequestBody InvoiceRequestDTO dto){
    return ResponseEntity.ok(invoiceService.createInvoice(dto));
  }

  @GetMapping("/All")
  public ResponseEntity<List<InvoiceResponseDTO>> getAllInvoice(){
    return ResponseEntity.ok(invoiceService.getAllInvoice());
  }
}
