package com.hoang.jobfinder.document;

import com.hoang.jobfinder.common.Enum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobDocument {
  private Long id;
  private String jobTitle;
  private String city;
  private Integer minSalary;
  private Integer maxSalary;
  private String description;
  private String requirement;
  private String benefit;
  private Long dueDateTimestamp;
  private Long postedAtTimestamp;
  private Enum.JobType jobType;
  private Enum.ExperienceLevel experienceLevel;
  private Enum.WorkplaceType workplaceType;
  private String companyName;
  private String companyAvatarUrl;
}
