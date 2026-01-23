package com.hoang.jobfinder.dto.auth.request;

import com.hoang.jobfinder.common.Const;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class HRSignUpRequestDTO {
  @Pattern(regexp = Const.Regex.EMAIL, message = "Email không đúng định dạng")
  private String email;

  @Pattern(regexp = Const.Regex.PASSWORD, message = "Mật khẩu phải chứa ít nhất 8 kí tự, bao gồm 1 kí tự hoa, 1 kí tự thường và 1 chữ số")
  private String password;

  private String fullName;

  private Long companyId;
}
