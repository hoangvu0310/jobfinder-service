package com.hoang.smartgrow.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignInRequestDTO {

  @NotBlank(message = "Tài khoản không được để trống")
  @Size(min = 6, message = "Tài khoản phải dài ít nhất 6 kí tự")
  private String username;

  @NotBlank(message = "Mật khẩu không được để trống")
  private String password;
}
