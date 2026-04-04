package com.example.learning.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class UserRequestDTO {

  @NotBlank
  private String userName;
  @NotBlank
  private String phone;
}
