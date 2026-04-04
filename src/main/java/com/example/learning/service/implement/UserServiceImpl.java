package com.example.learning.service.implement;

import com.example.learning.dto.request.UserRequestDTO;
import com.example.learning.dto.response.UserResponseDTO;
import com.example.learning.entity.User;
import com.example.learning.mapper.UserMapper;
import com.example.learning.repository.UserRepository;
import com.example.learning.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public UserResponseDTO createUser(UserRequestDTO dto) {
    User user = User.builder()
        .userName(dto.getUserName())
        .phone(dto.getPhone())
        .build();

    User user1 = userRepository.save(user);

    return userMapper.toResponse(user1);
  }
}
