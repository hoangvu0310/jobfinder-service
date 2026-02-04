package com.hoang.jobfinder.dto.job.response;

import com.hoang.jobfinder.common.Enum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class JobDTO {
  private Long jobId;
  private Enum.JobStatus jobStatus;
  private String jobTitle;
  private String city;
  private Integer minSalary;
  private Integer maxSalary;
  private String description;
  private String requirement;
  private String benefit;
  private LocalDate dueDate;
  private LocalDate postedAt;
  private Long companyId;
  private String companyName;
  private String companyAvatarUrl;
}
