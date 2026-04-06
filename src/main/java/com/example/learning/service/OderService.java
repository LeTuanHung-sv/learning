package com.example.learning.service;

import com.example.learning.dto.request.OderRequestDTO;
import com.example.learning.dto.response.OderResponseDTO;

public interface OderService {
  public OderResponseDTO create(OderRequestDTO oderRequestDTO);
}
