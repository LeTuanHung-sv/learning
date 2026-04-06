package com.example.learning.controller;

import com.example.learning.dto.response.OderResponseDTO;
import com.example.learning.service.OderService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OderController {
  private final OderService oderService;

  @GetMapping("/get-order/{id}")
  public ResponseEntity<OderResponseDTO> getById(@PathVariable UUID id){
    return ResponseEntity.ok(oderService.getOderId(id));
  }
}
