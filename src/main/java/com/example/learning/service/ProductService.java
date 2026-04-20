package com.example.learning.service;

import com.example.learning.dto.request.ProductRequestDTO;
import com.example.learning.dto.response.ProductResponseDTO;
import java.util.List;
import java.util.UUID;

public interface ProductService {
  public List<ProductResponseDTO> getProduct ();
  public ProductResponseDTO createProduct(ProductRequestDTO dto);
  public void deleteProducts(UUID id);
}
