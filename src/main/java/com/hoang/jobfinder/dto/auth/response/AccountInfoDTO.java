package com.hoang.jobfinder.dto.auth.response;

import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.entity.base.AccountBaseEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountInfoDTO {
  private Long userId;
  private String email;
  private Enum.AuthType authType;
  private Enum.Role role;

  public static AccountInfoDTO fromUser(AccountBaseEntity user) {
    return AccountInfoDTO.builder()
        .userId(user.getId())
        .email(user.getEmail())
        .authType(user.getAuthType())
        .role(user.getRole())
        .build();
  }
}
