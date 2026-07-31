package com.example.learning.controller;


import com.example.learning.dto.request.UpdateOrderStatusRequestDTO;
import com.example.learning.enums.OrderStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.learning.dto.request.OderRequestDTO;
import com.example.learning.dto.response.OderResponseDTO;
import com.example.learning.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class OrderController {
  private final OrderService oderService;

  @GetMapping("/orders/{id}")
  public ResponseEntity<OderResponseDTO> getById(@PathVariable UUID id){
    return ResponseEntity.ok(oderService.getOderId(id));
  }
  @PostMapping("/orders")
  public ResponseEntity<OderResponseDTO> createOder(@Valid @RequestBody OderRequestDTO oderRequestDTO){
    return ResponseEntity.ok(oderService.create(oderRequestDTO));
  }
  @GetMapping("/orders")
  public ResponseEntity<List<OderResponseDTO>> getAllOrders(){
    return ResponseEntity.ok(oderService.getAllOder());
  }

  @PatchMapping("/orders/{id}/pay")
  public ResponseEntity<OderResponseDTO> payOrder(@PathVariable UUID id){
    return ResponseEntity.ok(oderService.payOrder(id));
  }

  @PatchMapping("/orders/{id}/cancel")
  public ResponseEntity<OderResponseDTO> cancelOrder(@PathVariable UUID id){
    return ResponseEntity.ok(oderService.cancelOrder(id));
  }

  @PatchMapping("/orders/{id}/status")
  public ResponseEntity<String> updateStatus(@PathVariable UUID id, @RequestBody UpdateOrderStatusRequestDTO requestDTO){
    oderService.updateStatus(id, requestDTO.getOrderStatus());
    return ResponseEntity.ok("Update status success");
  }



}
