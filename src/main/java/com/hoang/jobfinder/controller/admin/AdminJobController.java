package com.hoang.jobfinder.controller.admin;

import com.hoang.jobfinder.common.Const;
import com.hoang.jobfinder.dto.ApiResponse;
import com.hoang.jobfinder.service.AdminJobService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Const.ADMIN_API_PREFIX + "/job")
@AllArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminJobController {
  private AdminJobService adminJobService;

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> deleteJob(@PathVariable Long id) {
    adminJobService.deleteJob(id);
    return ResponseEntity.ok(ApiResponse.successResponse());
  }
}
