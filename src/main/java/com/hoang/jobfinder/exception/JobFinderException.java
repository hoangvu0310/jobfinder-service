package com.hoang.jobfinder.exception;

import com.hoang.jobfinder.common.ResultCode;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class JobFinderException extends RuntimeException {

  private String errorCode;
  private String errorMessage;
  private HttpStatus status;

  public JobFinderException(String code, String message) {
    super("An error occur with code: " + code + " and message: " + message);
    this.errorCode = code;
    this.errorMessage = message;
    this.status = HttpStatus.BAD_REQUEST;
  }

  public JobFinderException(@NonNull ResultCode resultCode) {
    super("An error occur with code: " + resultCode.getCode() + " and message: " + resultCode.getMessage());
    this.errorCode = resultCode.getCode();
    this.errorMessage = resultCode.getMessage();
    this.status = resultCode.getHttpStatus();
  }
}
