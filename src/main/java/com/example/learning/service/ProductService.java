package com.example.learning.service;

import com.example.learning.dto.request.ProductRequestDTO;
import com.example.learning.dto.response.ProductResponseDTO;
import java.util.List;

public interface ProductService {
  public List<ProductResponseDTO> getProduct ();
  public ProductResponseDTO createProduct(ProductRequestDTO dto);

}
