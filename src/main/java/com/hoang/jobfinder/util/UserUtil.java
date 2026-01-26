package com.hoang.jobfinder.util;

import com.hoang.jobfinder.common.ErrorCode;
import com.hoang.jobfinder.dto.auth.response.AccountInfoDTO;
import com.hoang.jobfinder.exception.JobFinderException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class UserUtil {
  public static AccountInfoDTO getCurrentUser() {
    Object userDTO = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
    if (userDTO instanceof AccountInfoDTO) {
      return (AccountInfoDTO) userDTO;
    }
    throw new JobFinderException(ErrorCode.INVALID_CREDENTIALS, "Không tìm thấy người dùng");
  }
}
