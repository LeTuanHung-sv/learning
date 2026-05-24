package com.example.learning.controller;


import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.learning.dto.request.OderRequestDTO;
import com.example.learning.dto.response.OderResponseDTO;
import com.example.learning.service.OderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OderController {
  private final OderService oderService;

  @GetMapping("/get-order/{id}")
  public ResponseEntity<OderResponseDTO> getById(@PathVariable UUID id){
    return ResponseEntity.ok(oderService.getOderId(id));
  }
  @PostMapping("/create")
  public ResponseEntity<OderResponseDTO> createOder(@Valid @RequestBody OderRequestDTO oderRequestDTO){
    return ResponseEntity.ok(oderService.create(oderRequestDTO));
  }
  @GetMapping("/All")
  public ResponseEntity<List<OderResponseDTO>> getAllOrders(){
    return ResponseEntity.ok(oderService.getAllOder());
  }

  @PostMapping("/orderItem")
  public ResponseEntity<String> createOrderItem(@Valid @RequestBody OderRequestDTO dto){
    System.out.println(dto);
    oderService.createOrderItem(dto);
    return ResponseEntity.ok("Created");
  }

  @PutMapping("/{id}/pay")
  public ResponseEntity<OderResponseDTO> payOrder(@PathVariable UUID id){
    return ResponseEntity.ok(oderService.payOrder(id));
  }

  @PutMapping("/{id}/cancel")
  public ResponseEntity<OderResponseDTO> cancelOrder(@PathVariable UUID id){
    return ResponseEntity.ok(oderService.cancelOrder(id));
  }

  @GetMapping("/orders")
  public List<OderResponseDTO> getOderByUserId(@RequestParam UUID userId){
    return oderService.displayOrder(userId);
  }
}
