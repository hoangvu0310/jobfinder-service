package com.hoang.smartgrow.dto.auth.request;

import com.hoang.smartgrow.common.Const;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequestDTO {
  @Size(min = 6, message = "Tài khoản phải dài ít nhất 6 kí tự")
  private String username;

  @Pattern(regexp = Const.Regex.PASSWORD, message = "Mật khẩu phải chứa ít nhất 8 kí tự, bao gồm 1 kí tự hoa, 1 kí tự thường và 1 chữ số")
  private String password;

  @NotBlank(message = "Tên không được để trống")
  private String fullName;

  private String phoneNumber;

  @Pattern(regexp = Const.Regex.EMAIL, message = "Email không đúng định dạng")
  private String email;
}
