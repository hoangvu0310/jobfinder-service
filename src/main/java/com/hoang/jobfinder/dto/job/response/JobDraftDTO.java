package com.hoang.jobfinder.dto.job.response;

import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.dto.job.request.CreateEditJobRequestDTO;
import lombok.Data;

import java.time.Instant;

@Data
public class JobDraftDTO {
  private Long draftId;
  private Long jobId;
  private String requestDescription;
  private CreateEditJobRequestDTO payload;
  private Enum.CreateEditStatus status;
  private String postedBy;
  private Instant postedAt;
  private String handledBy;
  private Instant handledAt;
  private String rejectReason;
}
