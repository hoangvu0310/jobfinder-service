package com.hoang.jobfinder.service;

import com.hoang.jobfinder.dto.PageableResponse;
import com.hoang.jobfinder.dto.PagingDTO;
import com.hoang.jobfinder.dto.job.request.CreateEditJobRequestDTO;
import com.hoang.jobfinder.dto.job.request.JobFilterDTO;
import com.hoang.jobfinder.dto.job.response.JobDTO;
import com.hoang.jobfinder.dto.job.response.JobDraftDTO;
import com.hoang.jobfinder.dto.job.response.JobPreviewDTO;

public interface HRJobService {
  PageableResponse<JobPreviewDTO> findJobsByCompany(JobFilterDTO filterDTO, PagingDTO pagingDTO);
  JobDTO getJobInfo(Long jobId);
  JobDraftDTO uploadJob(CreateEditJobRequestDTO requestDTO);
  JobDraftDTO editJob(CreateEditJobRequestDTO requestDTO, Long jobId);
  void deleteJob(Long jobId);
}
