package com.example.learning.service;

import com.example.learning.dto.request.InventoryRequestDTO;
import com.example.learning.dto.response.InventoryResponseDTO;
import com.example.learning.entity.Inventory;
import java.util.UUID;

public interface InventoryService {
  public InventoryResponseDTO create (InventoryRequestDTO dto);
  public void updateInventory(UUID id, InventoryRequestDTO dto);
  public Inventory getInventoryProductId (UUID productId);
  public void restock (UUID productId, Integer quantity);
}
