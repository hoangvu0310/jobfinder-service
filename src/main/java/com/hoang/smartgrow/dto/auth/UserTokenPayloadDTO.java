package com.hoang.smartgrow.dto.auth;

import com.hoang.smartgrow.common.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserTokenPayloadDTO {

  private Long userId;

  private String username;

  private String fullName;

  private Role role;
}
