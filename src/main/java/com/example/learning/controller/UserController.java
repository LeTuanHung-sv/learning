package com.example.learning.controller;

import com.example.learning.dto.request.UserRequestDTO;
import com.example.learning.dto.response.UserResponseDTO;
import com.example.learning.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/User")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PostMapping("/crate")
  public ResponseEntity<UserResponseDTO> createUser (@Valid @RequestBody UserRequestDTO dto){
    return ResponseEntity.ok(userService.createUser(dto));
  }

}
