package com.hoang.jobfinder.dto.job;

import lombok.Data;

@Data
public class ReportJobRequestDTO {
  private Long jobId;
  private String reason;
}
