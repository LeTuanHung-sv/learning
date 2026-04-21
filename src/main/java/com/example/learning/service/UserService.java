package com.example.learning.service;

import com.example.learning.dto.request.UserRequestDTO;
import com.example.learning.dto.response.UserResponseDTO;
import java.util.List;
import java.util.UUID;

public interface UserService {
  public UserResponseDTO getUserById(UUID id);
  public UserResponseDTO createUser(UserRequestDTO dto);
  public List<UserResponseDTO> getAll(UserRequestDTO dto);
  public void updateUser(UUID id, UserRequestDTO dto);
  public void deleteUser(UUID id);
}
