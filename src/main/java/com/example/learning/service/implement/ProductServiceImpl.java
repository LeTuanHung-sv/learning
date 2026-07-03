package com.example.learning.service.implement;

import com.example.learning.dto.request.ProductRequestDTO;
import com.example.learning.dto.response.ProductResponseDTO;
import com.example.learning.entity.Inventory;
import com.example.learning.entity.Product;
import com.example.learning.exception.ResourceNotFoundException;
import com.example.learning.mapper.InventoryMapper;
import com.example.learning.mapper.ProductMapper;
import com.example.learning.repository.InventoryRepository;
import com.example.learning.repository.ProductRepository;
import com.example.learning.service.InventoryService;
import com.example.learning.service.ProductService;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;
  private final ProductMapper productMapper;
  private final InventoryRepository inventoryRepository;
  private final InventoryService inventoryService;



  @Override
  public List<ProductResponseDTO> getProduct() {
    return productRepository.findAll()
        .stream()
        .map(productMapper::toDTO)
        .toList();
  }
  
  @Override
  @Transactional
  public ProductResponseDTO createProduct(ProductRequestDTO dto){
    Product product = productMapper.toEntity(dto);
    Product product1 = productRepository.save(product);

    Inventory inventory = new Inventory();
    inventory.setProductId(product1.getProductId());
    inventory.setQuantity(BigDecimal.ZERO);
    inventoryService.createInventory(inventory);

    return productMapper.toDTO(product1);
  }

  @Override
  public ProductResponseDTO getProductsId(UUID id) {
    return productRepository.findById(id)
        .map(productMapper::toDTO)
        .orElseThrow(() -> new ResourceNotFoundException("product not found"));
  }

  @Override
  @Transactional
  public void updateProducts(UUID id, ProductRequestDTO dto) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("product not found"));

    if(dto.getProductName() != null){
      product.setProductName(dto.getProductName());
    }

    if(dto.getPrice() != null){
      product.setPrice(dto.getPrice());
    }

    productRepository.save(product);
  }
  
  @Override
  public void deleteProducts(UUID id){
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("product not found"));

    productRepository.delete(product);
  }
}
