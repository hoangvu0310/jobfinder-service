package com.hoang.jobfinder.dto.profile.request;

import lombok.Data;

@Data
public class HRProfileEditRequestDTO {
  private String fullName;
  private String title;
  private String description;
  private String phoneNumber;
}
