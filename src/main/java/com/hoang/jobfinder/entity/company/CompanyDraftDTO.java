package com.hoang.jobfinder.entity.company;

import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.dto.company.request.CompanyInfoPostRequestDTO;
import lombok.Data;

import java.time.Instant;

@Data
public class CompanyDraftDTO {
  private Long companyId;
  private CompanyInfoPostRequestDTO payload;
  private Enum.CreateEditStatus status;
  private Enum.ActionType actionType;
  private String postedBy;
  private Instant postedAt;
  private String handledBy;
  private Instant handledAt;
  private String rejectReason;
}
