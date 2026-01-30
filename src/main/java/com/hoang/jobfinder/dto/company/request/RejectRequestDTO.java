package com.hoang.jobfinder.dto.company.request;

import lombok.Data;

@Data
public class RejectRequestDTO {
  private Long draftId;
  private String rejectReason;
}
