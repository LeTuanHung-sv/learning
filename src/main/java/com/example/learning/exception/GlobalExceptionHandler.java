package com.example.learning.exception;

import com.example.learning.dto.response.ErrorResponse;
import java.time.LocalDateTime;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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

  @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
  public ResponseEntity<String> handleOptimisticLock(ObjectOptimisticLockingFailureException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body("Không thể đặt đơn hàng do cập nhật đồng thời. Vui lòng thử lại.");
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponse> handleBadRequest(
      BadRequestException ex){

    ErrorResponse response = ErrorResponse.builder()
        .status(HttpStatus.BAD_REQUEST.value())
        .error(HttpStatus.BAD_REQUEST.name())
        .message(ex.getMessage())
        .timestamp(LocalDateTime.now())
        .build();

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(response);
  }
}
