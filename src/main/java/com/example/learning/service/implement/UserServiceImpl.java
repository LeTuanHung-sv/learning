package com.example.learning.service.implement;

import com.example.learning.dto.request.UserRequestDTO;
import com.example.learning.dto.response.UserResponseDTO;
import java.util.List;
import java.util.UUID;
import com.example.learning.entity.User;
import com.example.learning.mapper.UserMapper;
import com.example.learning.repository.UserRepository;
import com.example.learning.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public UserResponseDTO getUserById(UUID id){
    return userRepository.findById(id)
        .map(userMapper::toResponse)
        .orElseThrow(()-> new RuntimeException("id not found"));
  }
  @Override
  public UserResponseDTO createUser(UserRequestDTO dto) {
    User user = User.builder()
        .userName(dto.getUserName())
        .phone(dto.getPhone())
        .build();

    User user1 = userRepository.save(user);

    return userMapper.toResponse(user1);
  }

  @Override
  public List<UserResponseDTO> getAll(UserRequestDTO dto) {
    return userRepository.findAll()
        .stream()
        .map(userMapper::toResponse)
        .toList();
  }
  
  @Override
  public void updateUser(UUID id, UserRequestDTO dto) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found"));

    if(dto.getUserName() != null)
      user.setUserName(dto.getUserName());

    if(dto.getPhone() != null)
      user.setPhone(dto.getPhone());

    userRepository.save(user);
  }
  
  @Override
  public void deleteUser(UUID id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("user not found"));
    userRepository.delete(user);
  }
}
