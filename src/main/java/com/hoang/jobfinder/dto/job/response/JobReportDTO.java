package com.hoang.jobfinder.dto.job.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class JobReportDTO {
  private Long userId;
  private String userAvatarUrl;
  private String userFullName;
  private String reason;
  private Instant createdAt;
}
