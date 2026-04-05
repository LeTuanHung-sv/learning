package com.example.learning.controller;

import com.example.learning.dto.request.ProductRequestDTO;
import com.example.learning.dto.response.ProductResponseDTO;
import com.example.learning.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
  private final ProductService productService;

  @PostMapping("/create")
  public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO dto){
    return ResponseEntity.ok(productService.createProduct(dto));
  }
}
