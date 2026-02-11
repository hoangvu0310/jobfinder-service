package com.hoang.jobfinder.service;

import com.hoang.jobfinder.dto.PageableResponse;
import com.hoang.jobfinder.dto.PagingDTO;
import com.hoang.jobfinder.dto.job.request.JobApplicationDTO;
import com.hoang.jobfinder.dto.job.response.JobDTO;
import com.hoang.jobfinder.dto.job.request.JobFilterDTO;
import com.hoang.jobfinder.dto.job.response.JobPreviewDTO;

public interface JobService {
  PageableResponse<JobPreviewDTO> findJob(JobFilterDTO filterDTO, PagingDTO pagingDTO);
  JobDTO findJobById(Long jobId);
  void applyJob(JobApplicationDTO jobApplicationDTO);
  void reportJob();
}
