package com.hoang.smartgrow.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResultCode {
  SUCCESS(ErrorCode.SUCCESS, "Thành công", HttpStatus.OK),

  VALIDATION_ERROR(ErrorCode.VALIDATION_ERROR, "Dữ liệu không hợp lệ", HttpStatus.BAD_REQUEST),
  UNAUTHORIZED(ErrorCode.UNAUTHORIZED, "Chưa đăng nhập", HttpStatus.UNAUTHORIZED),
  FORBIDDEN(ErrorCode.FORBIDDEN, "Không có quyền truy cập", HttpStatus.FORBIDDEN),
  NOT_FOUND(ErrorCode.NOT_FOUND, "Không tìm thấy dữ liệu", HttpStatus.NOT_FOUND),
  INTERNAL_ERROR(ErrorCode.INTERNAL_ERROR, "Lỗi hệ thống", HttpStatus.INTERNAL_SERVER_ERROR),
  BAD_REQUEST(ErrorCode.BAD_REQUEST, "Yêu cầu không hợp lệ", HttpStatus.BAD_REQUEST),
  INVALID_CREDENTIALS(ErrorCode.INVALID_CREDENTIALS, "Thông tin đăng nhập không chính xác", HttpStatus.UNAUTHORIZED),
  EXISTED_USER(ErrorCode.EXISTED_USER, "Tài khoản đã tồn tại", HttpStatus.CONFLICT);

  private final String code;
  private final String message;
  private final HttpStatus httpStatus;

  ResultCode(String code, String message, HttpStatus httpStatus) {
    this.code = code;
    this.message = message;
    this.httpStatus = httpStatus;
  }
}
