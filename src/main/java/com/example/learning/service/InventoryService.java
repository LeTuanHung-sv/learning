package com.example.learning.service;

import com.example.learning.dto.request.InventoryRequestDTO;
import com.example.learning.dto.response.InventoryResponseDTO;

public interface InventoryService {
  public InventoryResponseDTO create (InventoryRequestDTO dto);
}
