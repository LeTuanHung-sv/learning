package com.example.learning.service.implement;

import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.learning.dto.request.OderRequestDTO;
import com.example.learning.dto.response.OderResponseDTO;
import com.example.learning.entity.Oder;
import com.example.learning.mapper.OderMapper;
import com.example.learning.repository.OderRepository;
import com.example.learning.service.OderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OderServiceImpl implements OderService {
  private final OderRepository oderRepository;
  private final OderMapper oderMapper;

  @Override
  public OderResponseDTO getOderId(UUID id) {
    return oderRepository.findById(id)
        .map(oderMapper::toResponse)
        .orElseThrow(()-> new RuntimeException("id not found"));
  }

  @Override
  public OderResponseDTO create(OderRequestDTO oderRequestDTO) {
    Oder oder = oderMapper.toEntity(oderRequestDTO);

    Oder saved = oderRepository.save(oder);
    return oderMapper.toResponse(saved);
  }

  @Override
  public List<OderResponseDTO> getAllOder() {
    return oderRepository.findAll()
        .stream()
        .map(oderMapper::toResponse)
        .toList();
  }


}
