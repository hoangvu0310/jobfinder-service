package com.hoang.jobfinder.controller;

import com.hoang.jobfinder.common.Const;
import com.hoang.jobfinder.dto.ApiResponse;
import com.hoang.jobfinder.dto.PageableResponse;
import com.hoang.jobfinder.dto.PagingDTO;
import com.hoang.jobfinder.dto.job.request.JobApplicationDTO;
import com.hoang.jobfinder.dto.job.request.JobFilterDTO;
import com.hoang.jobfinder.dto.job.response.JobDTO;
import com.hoang.jobfinder.dto.job.response.JobPreviewDTO;
import com.hoang.jobfinder.service.JobService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Const.API_PREFIX + "/job")
@AllArgsConstructor
public class JobController {
  private JobService jobService;

  @PostMapping("/find")
  public ResponseEntity<ApiResponse<PageableResponse<JobPreviewDTO>>> findJob(
      @RequestBody JobFilterDTO filterDTO, @RequestParam Integer page, @RequestParam Integer size
  ) {
    return ResponseEntity.ok(ApiResponse.successResponse(jobService.findJob(filterDTO, new PagingDTO(page, size))));
  }

  @GetMapping("/detail/{id}")
  public ResponseEntity<ApiResponse<JobDTO>> findJobById(@PathVariable("id") Long jobId) {
    return ResponseEntity.ok(ApiResponse.successResponse(jobService.findJobById(jobId)));
  }

  @PostMapping("/apply")
  public ResponseEntity<ApiResponse<Void>> findJobById(@RequestBody JobApplicationDTO jobApplicationDTO) {
    jobService.applyJob(jobApplicationDTO);
    return ResponseEntity.ok(ApiResponse.successResponse());
  }
}
