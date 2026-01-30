package com.hoang.jobfinder.dto.company.response;

import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.dto.company.request.CompanyInfoPostRequestDTO;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class CompanyDraftDTO {
  private Long draftId;
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
