package com.hoang.jobfinder.service.impl;

import com.hoang.jobfinder.common.ErrorCode;
import com.hoang.jobfinder.exception.JobFinderException;
import com.hoang.jobfinder.repository.JobRepository;
import com.hoang.jobfinder.service.AdminJobService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AdminJobServiceImpl implements AdminJobService {
  private JobRepository jobRepository;

  @Override
  @Transactional
  public void deleteJob(Long jobId) {
    jobRepository.findById(jobId)
        .orElseThrow(() -> new JobFinderException(ErrorCode.NOT_FOUND, "Không tìm thấy thông tin công việc"));
    jobRepository.deleteById(jobId);
  }
}
