package com.example.learning.controller;

import com.example.learning.dto.request.InventoryRequestDTO;
import com.example.learning.dto.response.InventoryResponseDTO;
import com.example.learning.service.InventoryService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Inventories")
public class InventoryController {
  private final InventoryService inventoryService;

  @PostMapping("/create")
  public ResponseEntity<InventoryResponseDTO> createInventory(@Valid @RequestBody InventoryRequestDTO dto){
    return ResponseEntity.ok(inventoryService.create(dto));
  }

  @PutMapping("/{id}")
  public ResponseEntity<String> updateInventory(@PathVariable UUID id, @Valid @RequestBody InventoryRequestDTO dto){
    inventoryService.updateInventory(id, dto);
    return ResponseEntity.ok("Update success");
  }
}
