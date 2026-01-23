package com.hoang.jobfinder.dto.auth.request;

import com.hoang.jobfinder.common.Const;
import com.hoang.jobfinder.common.Enum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SignInRequestDTO {

  @Pattern(regexp = Const.Regex.EMAIL, message = "Email không đúng định dạng")
  @NotBlank(message = "Email không được để trống")
  private String email;

  @NotBlank(message = "Mật khẩu không được để trống")
  private String password;

  @NotBlank(message = "Id của thiết bị không được để trống")
  private String deviceId;

  private Enum.Platform platform;
}
