package com.example.learning.service.implement;

import com.example.learning.dto.request.InventoryRequestDTO;
import com.example.learning.dto.response.InventoryResponseDTO;
import com.example.learning.entity.Inventory;
import com.example.learning.mapper.InventoryMapper;
import com.example.learning.repository.InventoryRepository;
import com.example.learning.service.InventoryService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

  private final InventoryRepository inventoryRepository;
  private final InventoryMapper inventoryMapper;

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
}
