package com.example.learning.controller;

import com.example.learning.dto.request.UserRequestDTO;
import com.example.learning.dto.response.UserResponseDTO;
import com.example.learning.service.UserService;
import java.util.UUID;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDTO> getByIdUser(@PathVariable UUID id){
    return ResponseEntity.ok(userService.getUserById(id));
  }

  @PostMapping("/create")
  public ResponseEntity<UserResponseDTO> createUser (@Valid @RequestBody UserRequestDTO dto){
    return ResponseEntity.ok(userService.createUser(dto));
  }

  @PutMapping("/{id}")
  public ResponseEntity<String> updateUser(@PathVariable UUID id, @Valid @RequestBody UserRequestDTO dto){
    userService.updateUser(id,dto);
    return ResponseEntity.ok("Update success");
  }
  
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable UUID id){
    userService.deleteUser(id);
    return ResponseEntity.ok("Delete successfully");
  }
}
