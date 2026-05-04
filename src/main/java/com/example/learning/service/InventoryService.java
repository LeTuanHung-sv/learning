package com.example.learning.service;

import com.example.learning.dto.request.InventoryRequestDTO;
import com.example.learning.dto.response.InventoryResponseDTO;
import com.example.learning.entity.Inventory;
import java.util.UUID;

public interface InventoryService {
  public InventoryResponseDTO create (InventoryRequestDTO dto);
  public Inventory getInventoryProductId (UUID productId);
}
