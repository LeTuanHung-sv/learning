package com.example.learning.mapper;

import com.example.learning.dto.response.ProductResponseDTO;
import com.example.learning.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
  Product Entity(ProductResponseDTO dto);
  ProductResponseDTO toDTO(Product product);
}
