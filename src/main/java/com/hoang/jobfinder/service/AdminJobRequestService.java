package com.hoang.jobfinder.service;

import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.dto.PageableResponse;
import com.hoang.jobfinder.dto.PagingDTO;
import com.hoang.jobfinder.dto.company.request.RejectRequestDTO;
import com.hoang.jobfinder.dto.job.response.JobDTO;
import com.hoang.jobfinder.dto.job.response.JobDraftDTO;

public interface AdminJobRequestService {
  PageableResponse<JobDraftDTO> getAllJobDrafts(PagingDTO pagingDTO, Enum.ActionType actionType);

  JobDTO getJobRequestById(Long draftId);

  JobDraftDTO approveCreateJob(Long draftId);

  JobDraftDTO approveEditJob(Long draftId);

  JobDraftDTO rejectJob(RejectRequestDTO rejectRequestDTO);
}
