package com.example.learning.controller;

import com.example.learning.dto.request.UserRequestDTO;
import com.example.learning.dto.response.UserResponseDTO;
import com.example.learning.service.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("User")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDTO> getByIdUser(@PathVariable UUID id, UserRequestDTO dto){
    return ResponseEntity.ok(userService.getUserById(id,dto));
  }
}
