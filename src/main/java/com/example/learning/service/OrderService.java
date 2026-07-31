package com.example.learning.service;

import com.example.learning.dto.response.OderResponseDTO;
import com.example.learning.dto.request.OderRequestDTO;
import com.example.learning.enums.OrderStatus;
import java.util.List;
import java.util.UUID;

public interface OrderService {
  public OderResponseDTO getOderId(UUID id);
  public OderResponseDTO create(OderRequestDTO oderRequestDTO);
  public List<OderResponseDTO> getAllOder();
 // public void createOrderItem(OderRequestDTO request);
  public OderResponseDTO payOrder(UUID id);
  public OderResponseDTO cancelOrder(UUID id);
  public void updateStatus(UUID id, OrderStatus status);
}
