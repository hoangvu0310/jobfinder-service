package com.hoang.smartgrow.dto.auth.response;

import com.hoang.smartgrow.common.Role;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class UserResponseDTO {
  private Long userId;
  private String username;
  private String email;
  private String phoneNumber;
  private String fullName;
  private Role role;
  private Instant createdAt;
  private String createBy;
}
