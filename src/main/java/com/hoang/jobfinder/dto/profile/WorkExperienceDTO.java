package com.hoang.jobfinder.dto.profile;

import lombok.Data;

import java.time.Instant;

@Data
public class WorkExperienceDTO {
  private Long id;
  private String jobTitle;
  private String company;
  private Instant startTime;
  private Instant endTime;
  private String description;
  private Integer positionInList;
}
