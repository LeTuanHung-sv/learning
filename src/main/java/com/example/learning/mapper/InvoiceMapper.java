package com.example.learning.mapper;

import com.example.learning.dto.request.InvoiceRequestDTO;
import com.example.learning.dto.response.InvoiceResponseDTO;
import com.example.learning.entity.Invoice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
  Invoice toEntity(InvoiceRequestDTO dto);
  InvoiceResponseDTO toResponse(Invoice invoice);

}
