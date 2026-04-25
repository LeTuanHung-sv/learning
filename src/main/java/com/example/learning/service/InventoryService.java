package com.example.learning.service;

import com.example.learning.dto.request.InventoryRequestDTO;
import com.example.learning.dto.response.InventoryResponseDTO;
import java.util.UUID;

public interface InventoryService {
  public InventoryResponseDTO create (InventoryRequestDTO dto);
  public void updateInventory(UUID id, InventoryRequestDTO dto);

}
