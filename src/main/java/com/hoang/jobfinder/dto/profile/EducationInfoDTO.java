package com.hoang.jobfinder.dto.profile;

import lombok.Data;

import java.time.Instant;

@Data
public class EducationInfoDTO {
  private Long id;
  private String major;
  private String facility;
  private Instant startTime;
  private Instant endTime;
  private Integer positionInList;
}
