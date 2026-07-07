package com.example.learning.dto.request;


import com.example.learning.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OderRequestDTO {
  @NotNull
  private UUID userId;
  @NotNull
  private PaymentMethod paymentMethod;
  @NotBlank
  private String shippingAddress;
  @NotNull
  @NotEmpty
  @Valid
  private List<OderItemRequestDTO> items;


}
