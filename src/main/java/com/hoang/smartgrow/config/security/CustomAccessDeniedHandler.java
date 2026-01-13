package com.hoang.smartgrow.config.security;

import com.hoang.smartgrow.dto.ApiResponse;
import com.hoang.smartgrow.common.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@AllArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
  private ObjectMapper objectMapper;

  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException
  ) throws IOException {
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.getOutputStream().write(objectMapper.writeValueAsBytes(ApiResponse.errorResponse(ResultCode.FORBIDDEN)));
  }
}
