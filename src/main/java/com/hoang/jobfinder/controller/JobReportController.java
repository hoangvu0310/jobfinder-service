package com.hoang.jobfinder.controller;

import com.hoang.jobfinder.common.Const;
import com.hoang.jobfinder.dto.ApiResponse;
import com.hoang.jobfinder.dto.job.ReportJobRequestDTO;
import com.hoang.jobfinder.dto.job.response.JobReportDTO;
import com.hoang.jobfinder.service.JobReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Const.API_PREFIX + "/jobReport")
@AllArgsConstructor
public class JobReportController {
  private JobReportService jobReportService;

  @GetMapping("/job/{jobId}/all")
  public ResponseEntity<ApiResponse<List<JobReportDTO>>> getAllJobReports(@PathVariable Long jobId) {
    return ResponseEntity.ok(ApiResponse.successResponse(jobReportService.getAllReportsByJobId(jobId)));
  }

  @GetMapping("/detail/{reportId}")
  public ResponseEntity<ApiResponse<JobReportDTO>> getReportByid(@PathVariable Long reportId) {
    return ResponseEntity.ok(ApiResponse.successResponse(jobReportService.getReportById(reportId)));
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  @PostMapping("/report")
  public ResponseEntity<ApiResponse<JobReportDTO>> reportJob(@RequestBody ReportJobRequestDTO reportJobRequestDTO) {
    return ResponseEntity.ok(ApiResponse.successResponse(jobReportService.reportJob(reportJobRequestDTO)));
  }

  @PatchMapping("/update/{reportId}")
  public ResponseEntity<ApiResponse<JobReportDTO>> updateReport(
      @PathVariable Long reportId, @RequestBody ReportJobRequestDTO reportJobRequestDTO
  ) {
    return ResponseEntity.ok(ApiResponse.successResponse(jobReportService.updateReport(reportId, reportJobRequestDTO)));
  }

  @DeleteMapping("/delete/{reportId}")
  public ResponseEntity<ApiResponse<Void>> deleteReport(@PathVariable Long reportId) {
    jobReportService.deleteReport(reportId);
    return ResponseEntity.ok(ApiResponse.successResponse());
  }
}
