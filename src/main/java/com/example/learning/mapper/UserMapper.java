package com.example.learning.mapper;

import com.example.learning.dto.response.UserResponseDTO;
import com.example.learning.entity.User;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {
  User toEntity(UserResponseDTO userResponseDTO);

  UserResponseDTO toResponse(User user);
}
