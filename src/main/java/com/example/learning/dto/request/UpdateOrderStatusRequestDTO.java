package com.example.learning.dto.request;

import com.example.learning.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderStatusRequestDTO {
  private OrderStatus orderStatus;
}
