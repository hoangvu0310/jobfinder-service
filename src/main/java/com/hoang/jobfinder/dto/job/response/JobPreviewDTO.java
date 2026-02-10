package com.hoang.jobfinder.dto.job.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class JobPreviewDTO {
  private Long jobId;
  private String jobTitle;
  private String city;
  private Integer minSalary;
  private Integer maxSalary;
  private Instant postedAt;
  private String companyName;
  private String companyAvatarUrl;
}
