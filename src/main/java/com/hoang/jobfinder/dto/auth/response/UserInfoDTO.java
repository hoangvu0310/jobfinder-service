package com.hoang.jobfinder.dto.auth.response;

import com.hoang.jobfinder.common.Enum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoDTO {
  private Long userId;
  private String username;
  private String email;
  private String phoneNumber;
  private String fullName;
  private Enum.Role role;
}
