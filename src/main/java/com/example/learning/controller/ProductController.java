package com.example.learning.controller;

import com.example.learning.dto.response.ProductResponseDTO;
import com.example.learning.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
  private final ProductService productService;

  @GetMapping("/getAll")
  public ResponseEntity<List<ProductResponseDTO>> getProduct(){
    return ResponseEntity.ok(productService.getProduct());
  }
}
