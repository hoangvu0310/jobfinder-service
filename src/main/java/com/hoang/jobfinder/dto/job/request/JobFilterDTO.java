package com.hoang.jobfinder.dto.job.request;

import com.hoang.jobfinder.common.Enum;
import lombok.Data;

@Data
public class JobFilterDTO {
  private String searchText;
  private String city;
  private Integer minSalary;
  private Integer maxSalary;
  private Enum.ExperienceLevel experienceLevel;
  private Enum.JobType jobType;
  private Enum.WorkplaceType workplaceType;
  private Enum.JobStatus jobStatus;
}
