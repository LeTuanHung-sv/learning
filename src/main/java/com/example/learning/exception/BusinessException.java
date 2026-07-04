package com.example.learning.exception;

public class BusinessException extends RuntimeException{
  public BusinessException(String message) {
    super(message);
  }
}
