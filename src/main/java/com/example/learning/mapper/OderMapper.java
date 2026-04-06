package com.example.learning.mapper;

import com.example.learning.dto.request.OderRequestDTO;
import com.example.learning.dto.response.OderResponseDTO;
import com.example.learning.entity.Oder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OderMapper {
  Oder toEntity(OderRequestDTO dto);
  OderResponseDTO toResponse(Oder oder);
}
