package com.example.learning.service;

import com.example.learning.dto.response.OderResponseDTO;
import com.example.learning.dto.request.OderRequestDTO;
import java.util.List;
import java.util.UUID;

public interface OderService {
  public OderResponseDTO getOderId(UUID id);
  public OderResponseDTO create(OderRequestDTO oderRequestDTO);
  public List<OderResponseDTO> getAllOder();
}
