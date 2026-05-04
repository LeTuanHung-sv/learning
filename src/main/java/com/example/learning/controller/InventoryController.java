package com.example.learning.controller;

import com.example.learning.dto.request.InventoryRequestDTO;
import com.example.learning.dto.response.InventoryResponseDTO;
import com.example.learning.entity.Inventory;
import com.example.learning.service.InventoryService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

  @GetMapping("/{productId}")
  public ResponseEntity<Inventory> getInventoryProductId(@PathVariable UUID productId){
    Inventory inventory = inventoryService.getInventoryProductId(productId);
    return ResponseEntity.ok(inventory);
  }
}
