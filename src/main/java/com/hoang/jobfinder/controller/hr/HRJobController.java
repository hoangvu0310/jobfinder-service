package com.hoang.jobfinder.controller.hr;

import com.hoang.jobfinder.common.Const;
import com.hoang.jobfinder.dto.ApiResponse;
import com.hoang.jobfinder.dto.PageableResponse;
import com.hoang.jobfinder.dto.PagingDTO;
import com.hoang.jobfinder.dto.job.request.CreateEditJobRequestDTO;
import com.hoang.jobfinder.dto.job.request.JobFilterDTO;
import com.hoang.jobfinder.dto.job.response.JobDTO;
import com.hoang.jobfinder.dto.job.response.JobDraftDTO;
import com.hoang.jobfinder.dto.job.response.JobPreviewDTO;
import com.hoang.jobfinder.service.HRJobService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Const.HR_API_PREFIX + "/job")
@AllArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_HR_ADMIN', 'ROLE_HR')")
public class HRJobController {
  private HRJobService hrJobService;

  @PostMapping("/company")
  public ResponseEntity<ApiResponse<PageableResponse<JobPreviewDTO>>> getAllJobByCompanyId(
      @RequestBody JobFilterDTO jobFilterDTO, @RequestParam Integer page, @RequestParam Integer size
      ) {
    return ResponseEntity.ok(
        ApiResponse.successResponse(hrJobService.findJobsByCompany(jobFilterDTO, new PagingDTO(page, size)))
    );
  }

  @GetMapping("/detail/{id}")
  public ResponseEntity<ApiResponse<JobDTO>> getJobById(@PathVariable("id") Long jobId) {
    return ResponseEntity.ok(ApiResponse.successResponse(hrJobService.getJobInfo(jobId)));
  }

  @PostMapping("/upload")
  public ResponseEntity<ApiResponse<JobDraftDTO>> getAllJobByCompanyId(
      @Valid @RequestBody CreateEditJobRequestDTO createEditJobRequestDTO
  ) {
    return ResponseEntity.ok(
        ApiResponse.successResponse(hrJobService.uploadJob(createEditJobRequestDTO))
    );
  }

  @PatchMapping("/edit/{id}")
  public ResponseEntity<ApiResponse<JobDraftDTO>> editJob(
      @Valid @RequestBody CreateEditJobRequestDTO createEditJobRequestDTO, @PathVariable("id") Long jobId
  ) {
    return ResponseEntity.ok(
        ApiResponse.successResponse(hrJobService.editJob(createEditJobRequestDTO, jobId))
    );
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteJob(@PathVariable("id") Long jobId) {
    hrJobService.deleteJob(jobId);
    return ResponseEntity.ok(ApiResponse.successResponse());
  }

}
