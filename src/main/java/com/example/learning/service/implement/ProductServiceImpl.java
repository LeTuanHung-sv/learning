package com.example.learning.service.implement;

import com.example.learning.dto.request.ProductRequestDTO;
import com.example.learning.dto.response.ProductResponseDTO;
import com.example.learning.entity.Product;
import com.example.learning.mapper.ProductMapper;
import com.example.learning.repository.ProductRepository;
import com.example.learning.service.ProductService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;
  private final ProductMapper productMapper;

  @Override
  public List<ProductResponseDTO> getProduct() {
    return productRepository.findAll()
        .stream()
        .map(productMapper::toDTO)
        .toList();
  }
  @Override
  public ProductResponseDTO createProduct(ProductRequestDTO dto){
    Product product = Product.builder()
        .productName(dto.getProductName())
        .price(dto.getPrice())
        .productStatus(dto.getProductStatus())
        .build();

    Product product1 = productRepository.save(product);
    return productMapper.toDTO(product1);
  }

  @Override
  public ProductResponseDTO getProductsId(UUID id) {
    return productRepository.findById(id)
        .map(productMapper::toDTO)
        .orElseThrow(() -> new RuntimeException("product not found"));
  }
}
