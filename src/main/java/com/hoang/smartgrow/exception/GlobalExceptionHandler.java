package com.hoang.smartgrow.exception;

import com.hoang.smartgrow.dto.ApiResponse;
import com.hoang.smartgrow.common.ErrorCode;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(SmartGrowException.class)
  public ResponseEntity<ApiResponse<Void>> handleServiceException(SmartGrowException exception) {
    log.error("Service exception occur with reason: {}", exception.getErrorMessage(), exception);
    return ResponseEntity
        .status(exception.getStatus())
        .body(ApiResponse.errorResponse(exception.getErrorCode(), exception.getErrorMessage()));
  }

  @ExceptionHandler(AuthorizationDeniedException.class)
  public ResponseEntity<ApiResponse<Void>> handleAuthorizationDenied(AuthorizationDeniedException exception) {
    log.error("Authorization denied occur with reason: {}", exception.getMessage(), exception);
    return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body(ApiResponse.errorResponse(ErrorCode.FORBIDDEN, exception.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException exception) {
    log.error("Method Argument Invalid exception occur with reason: {}", exception.getMessage(), exception);

    String message = exception.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(e -> e.getField() + ": " + e.getDefaultMessage())
        .collect(Collectors.joining(", "));

    return ResponseEntity
        .badRequest()
        .body(ApiResponse.errorResponse(ErrorCode.VALIDATION_ERROR, message));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiResponse<Void>> handleConstraint(ConstraintViolationException exception) {
    log.error("Constraint Violation exception occur with reason: {}", exception.getMessage(), exception);
    return ResponseEntity
        .badRequest()
        .body(ApiResponse.errorResponse(ErrorCode.VALIDATION_ERROR, exception.getMessage()));
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ApiResponse<Void>> handleResponseStatus(ResponseStatusException exception) {
    log.error("Response Status exception occur with reason: {}", exception.getMessage(), exception);
    return ResponseEntity
        .status(exception.getStatusCode())
        .body(ApiResponse.errorResponse(exception.getStatusCode().toString(), exception.getReason()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> handleAll(Exception exception) {
    log.error("Unknown exception occur with reason: {}", exception.getMessage(), exception);
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.errorResponse(ErrorCode.INTERNAL_ERROR, exception.getMessage()));
  }
}
