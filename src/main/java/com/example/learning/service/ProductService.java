package com.example.learning.service;

import com.example.learning.dto.request.ProductRequestDTO;
import com.example.learning.dto.response.ProductResponseDTO;

public interface ProductService {
  public ProductResponseDTO createProduct(ProductRequestDTO dto);
}
