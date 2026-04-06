package com.example.learning.service;

import com.example.learning.dto.response.OderResponseDTO;
import java.util.UUID;

public interface OderService {
  public OderResponseDTO getOderId(UUID id);
}
