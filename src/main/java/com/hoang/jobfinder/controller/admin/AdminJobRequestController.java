package com.hoang.jobfinder.controller.admin;

import com.hoang.jobfinder.common.Const;
import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.dto.ApiResponse;
import com.hoang.jobfinder.dto.PageableResponse;
import com.hoang.jobfinder.dto.PagingDTO;
import com.hoang.jobfinder.dto.company.request.RejectRequestDTO;
import com.hoang.jobfinder.dto.job.response.JobDTO;
import com.hoang.jobfinder.dto.job.response.JobDraftDTO;
import com.hoang.jobfinder.service.AdminJobRequestService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Const.ADMIN_API_PREFIX + "/jobRequest")
@AllArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminJobRequestController {
  private AdminJobRequestService adminJobRequestService;

  @GetMapping("/")
  public ResponseEntity<ApiResponse<PageableResponse<JobDraftDTO>>> getAllJobRequest(
      @RequestParam Integer page, @RequestParam Integer size, @RequestParam(required = false)Enum.ActionType actionType
  ) {
    return ResponseEntity.ok(ApiResponse.successResponse(adminJobRequestService.getAllJobDrafts(new PagingDTO(page, size), actionType)));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<JobDTO>> getJobRequestById(@PathVariable Long id) {
    return ResponseEntity.ok(ApiResponse.successResponse(adminJobRequestService.getJobRequestById(id)));
  }

  @PostMapping("/{id}/approveCreate")
  public ResponseEntity<ApiResponse<JobDraftDTO>> approveCreateJob(@PathVariable Long id) {
    return ResponseEntity.ok(ApiResponse.successResponse(adminJobRequestService.approveCreateJob(id)));
  }

  @PostMapping("/{id}/approveEdit")
  public ResponseEntity<ApiResponse<JobDraftDTO>> approveEditJob(@PathVariable Long id) {
    return ResponseEntity.ok(ApiResponse.successResponse(adminJobRequestService.approveEditJob(id)));
  }

  @PatchMapping("/reject")
  public ResponseEntity<ApiResponse<JobDraftDTO>> rejectRequest(RejectRequestDTO rejectRequestDTO) {
    return ResponseEntity.ok(ApiResponse.successResponse(adminJobRequestService.rejectJob(rejectRequestDTO)));
  }
}
