package com.example.learning.controller;

import com.example.learning.dto.request.OderRequestDTO;
import com.example.learning.dto.response.OderResponseDTO;
import com.example.learning.service.OderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OderController {
  public final OderService oderService;

  @PostMapping("/create")
  public ResponseEntity<OderResponseDTO> createOder(@Valid @RequestBody OderRequestDTO oderRequestDTO){
    return ResponseEntity.ok(oderService.create(oderRequestDTO));
  }
}
