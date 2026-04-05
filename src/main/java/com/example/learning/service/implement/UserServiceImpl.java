package com.example.learning.service.implement;

import com.example.learning.dto.request.UserRequestDTO;
import com.example.learning.dto.response.UserResponseDTO;
import com.example.learning.mapper.UserMapper;
import com.example.learning.repository.UserRepository;
import com.example.learning.service.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserResponseDTO getUserById(UUID id, UserRequestDTO dto){
    return userRepository.findById(id)
        .map(userMapper::toResponse)
        .orElseThrow(()-> new RuntimeException("id not found"));
  }
}
