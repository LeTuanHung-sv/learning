package com.example.learning.service.implement;

import com.example.learning.dto.request.InventoryRequestDTO;
import com.example.learning.dto.response.InventoryResponseDTO;
import com.example.learning.entity.Inventory;
import com.example.learning.entity.Product;
import com.example.learning.mapper.InventoryMapper;
import com.example.learning.repository.InventoryRepository;
import com.example.learning.repository.ProductRepository;
import com.example.learning.service.InventoryService;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

  private final InventoryRepository inventoryRepository;
  private final InventoryMapper inventoryMapper;
  private final ProductRepository productRepository;

  @Override
  public InventoryResponseDTO create(InventoryRequestDTO dto){
    Inventory inventory = inventoryMapper.toEntity(dto);

    Inventory saved = inventoryRepository.save(inventory);
    return inventoryMapper.toResponse(saved);
  }

  @Override
  public void updateInventory(UUID id, InventoryRequestDTO dto) {
    Inventory inventory = inventoryRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Inventory not found"));

    if(dto.getProductId() != null)
      inventory.setProductId(dto.getProductId());

    if(dto.getQuantity() != null)
      inventory.setQuantity(dto.getQuantity());

    inventoryRepository.save(inventory);
}
  @Override
  public Inventory getInventoryProductId(UUID productId) {
    return inventoryRepository.findByProductId(productId)
        .orElseThrow(() -> new RuntimeException("Inventory not found"));
  }

  @Override
  public void restock(UUID productId, Integer quantity) {
    Inventory inventory = inventoryRepository.findByProductId(productId)
        .orElseThrow(() -> new RuntimeException("product not found"));

    Integer SoCu = inventory.getQuantity();
    Integer SoMoi = SoCu + quantity;

    inventory.setQuantity(SoMoi);

    inventoryRepository.save(inventory);
  }
}
