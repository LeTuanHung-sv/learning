package com.example.learning.service;

import com.example.learning.dto.request.UserRequestDTO;
import com.example.learning.dto.response.UserResponseDTO;
import java.util.UUID;

public interface UserService {
  public UserResponseDTO getUserById(UUID id, UserRequestDTO dto);
  public UserResponseDTO createUser(UserRequestDTO dto);
}
