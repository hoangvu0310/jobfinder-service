package com.hoang.jobfinder.service;

import com.hoang.jobfinder.dto.job.ReportJobRequestDTO;
import com.hoang.jobfinder.dto.job.response.JobReportDTO;

import java.util.List;

public interface JobReportService {
  List<JobReportDTO> getAllReportsByJobId(Long jobId);

  JobReportDTO getReportById(Long reportId);

  JobReportDTO reportJob(ReportJobRequestDTO reportJobRequestDTO);

  JobReportDTO updateReport(Long reportId, ReportJobRequestDTO reportJobRequestDTO);

  void deleteReport(Long reportId);
}
