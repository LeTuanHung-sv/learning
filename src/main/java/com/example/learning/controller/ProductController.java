package com.example.learning.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.learning.dto.request.ProductRequestDTO;
import com.example.learning.dto.response.ProductResponseDTO;
import com.example.learning.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
  
  @PostMapping("/create")
  public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO dto){
    return ResponseEntity.ok(productService.createProduct(dto));
  }
  
  @GetMapping("/{id}")
  public ResponseEntity<ProductResponseDTO> getProductsId(@PathVariable UUID id)
  {
    return ResponseEntity.ok(productService.getProductsId(id));

  @PutMapping("/{id}")
  public ResponseEntity<String> updateProduct(@PathVariable UUID id, @Valid @RequestBody ProductRequestDTO dto){
    productService.updateProducts(id,dto);
    return ResponseEntity.ok("Products success");
  }
  
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteProduct(@PathVariable UUID id){
    productService.deleteProducts(id);
    return ResponseEntity.ok("Delete successfully");
  }
}
