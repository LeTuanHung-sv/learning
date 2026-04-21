package com.example.learning.mapper;

import com.example.learning.dto.request.InventoryRequestDTO;
import com.example.learning.dto.response.InventoryResponseDTO;
import com.example.learning.entity.Inventory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
  Inventory toEntity(InventoryRequestDTO dto);
  InventoryResponseDTO toResponse(Inventory inventory);
}
