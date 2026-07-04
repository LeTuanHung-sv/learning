package com.example.learning.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<String> handleNotFound(
      ResourceNotFoundException ex) {

    return ResponseEntity.status(404)
        .body(ex.getMessage());
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<String> handleBusiness(
      BusinessException ex) {

    return ResponseEntity.status(400)
        .body(ex.getMessage());
  }
}
