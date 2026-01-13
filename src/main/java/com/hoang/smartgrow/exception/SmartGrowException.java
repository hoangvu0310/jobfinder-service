package com.hoang.smartgrow.exception;

import com.hoang.smartgrow.common.ResultCode;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class SmartGrowException extends RuntimeException {

  private String errorCode;
  private String errorMessage;
  private HttpStatus status;

  public SmartGrowException(String code, String message) {
    super("An error occur with code: " + code + " and message: " + message);
    this.errorCode = code;
    this.errorMessage = message;
    this.status = HttpStatus.BAD_REQUEST;
  }

  public SmartGrowException(@NonNull ResultCode resultCode) {
    super("An error occur with code: " + resultCode.getCode() + " and message: " + resultCode.getMessage());
    this.errorCode = resultCode.getCode();
    this.errorMessage = resultCode.getMessage();
    this.status = resultCode.getHttpStatus();
  }
}
