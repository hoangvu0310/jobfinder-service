package com.hoang.jobfinder.dto;

import com.hoang.jobfinder.common.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ApiResponse<T> {
  private T data;

  private String resultCode;

  private String resultMessage;

  private Instant timestamp;

  public static <T> ApiResponse<T> successResponse(T data) {
    return ApiResponse.<T>builder()
        .data(data)
        .resultCode(ResultCode.SUCCESS.getCode())
        .resultMessage(ResultCode.SUCCESS.getMessage())
        .timestamp(Instant.now())
        .build();
  }

  public static ApiResponse<Void> successResponse() {
    return ApiResponse.<Void>builder()
        .resultCode(ResultCode.SUCCESS.getCode())
        .resultMessage(ResultCode.SUCCESS.getMessage())
        .timestamp(Instant.now())
        .build();
  }

  public static ApiResponse<Void> errorResponse(ResultCode resultCode) {
    return ApiResponse.<Void>builder()
        .resultCode(resultCode.getCode())
        .resultMessage(resultCode.getMessage())
        .timestamp(Instant.now())
        .build();
  }

  public static ApiResponse<Void> errorResponse(String code, String message) {
    return ApiResponse.<Void>builder()
        .resultCode(code)
        .resultMessage(message)
        .timestamp(Instant.now())
        .build();
  }
}
