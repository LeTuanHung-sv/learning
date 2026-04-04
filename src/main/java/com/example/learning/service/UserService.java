package com.example.learning.service;

import com.example.learning.dto.request.UserRequestDTO;
import com.example.learning.dto.response.UserResponseDTO;

public interface UserService {
  public UserResponseDTO createUser(UserRequestDTO dto);
}
