package com.hoang.jobfinder.service.impl;

import com.hoang.jobfinder.common.ErrorCode;
import com.hoang.jobfinder.common.ResultCode;
import com.hoang.jobfinder.dto.job.ReportJobRequestDTO;
import com.hoang.jobfinder.dto.job.response.JobReportDTO;
import com.hoang.jobfinder.entity.job.Job;
import com.hoang.jobfinder.entity.job.JobReport;
import com.hoang.jobfinder.entity.user.User;
import com.hoang.jobfinder.entity.user.UserProfile;
import com.hoang.jobfinder.exception.JobFinderException;
import com.hoang.jobfinder.repository.JobReportRepository;
import com.hoang.jobfinder.repository.JobRepository;
import com.hoang.jobfinder.repository.UserRepository;
import com.hoang.jobfinder.service.JobReportService;
import com.hoang.jobfinder.service.SupabaseS3Service;
import com.hoang.jobfinder.util.UserUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class JobReportServiceImpl implements JobReportService {
  private JobReportRepository jobReportRepository;

  private JobRepository jobRepository;

  private UserRepository userRepository;

  private SupabaseS3Service supabaseS3Service;

  @Override
  public List<JobReportDTO> getAllReportsByJobId(Long jobId) {
    List<JobReportDTO> reportDTOList = jobReportRepository.findReportsByJobId(jobId);
    reportDTOList.forEach(
        report -> report.setUserAvatarUrl(
            supabaseS3Service.generatePublicGetUrl(report.getUserAvatarUrl())
        )
    );

    return reportDTOList;
  }

  @Override
  public JobReportDTO getReportById(Long reportId) {
    JobReport report = jobReportRepository.findById(reportId)
        .orElseThrow(() -> new JobFinderException(ErrorCode.NOT_FOUND, "Không tìm thấy thông tin báo cáo"));
    User user = report.getUser();
    UserProfile userProfile = user.getUserProfile();

    return JobReportDTO.builder()
        .userId(user.getId())
        .userAvatarUrl(supabaseS3Service.generatePublicGetUrl(userProfile.getAvatarUrlKey()))
        .userFullName(userProfile.getFullName())
        .reason(report.getReason())
        .createdAt(report.getCreatedAt())
        .build();
  }

  @Override
  public JobReportDTO reportJob(ReportJobRequestDTO reportJobRequestDTO) {
    Job job = jobRepository.findById(reportJobRequestDTO.getJobId())
        .orElseThrow(() -> new JobFinderException(ErrorCode.NOT_FOUND, "Không tìm thấy thông tin báo cáo"));
    User user = userRepository.findById(UserUtil.getCurrentUser().getUserId())
        .orElseThrow(() -> new JobFinderException(ErrorCode.NOT_FOUND, "Không tìm thấy thông tin người dùng"));

    JobReport newReport = JobReport.builder()
        .reason(reportJobRequestDTO.getReason())
        .user(user)
        .job(job)
        .build();

    jobReportRepository.save(newReport);

    return null;
  }

  @Override
  @Transactional
  public JobReportDTO updateReport(Long reportId, ReportJobRequestDTO reportJobRequestDTO) {
    JobReport report = jobReportRepository.findById(reportId)
        .orElseThrow(() -> new JobFinderException(ErrorCode.NOT_FOUND, "Không tìm thấy thông tin báo cáo"));
    report.setReason(reportJobRequestDTO.getReason());

    User user = report.getUser();
    UserProfile userProfile = user.getUserProfile();

    return JobReportDTO.builder()
        .userId(user.getId())
        .userAvatarUrl(supabaseS3Service.generatePublicGetUrl(userProfile.getAvatarUrlKey()))
        .userFullName(userProfile.getFullName())
        .reason(report.getReason())
        .createdAt(report.getCreatedAt())
        .build();
  }

  @Override
  public void deleteReport(Long reportId) {
    JobReport report = jobReportRepository.findById(reportId)
        .orElseThrow(() -> new JobFinderException(ErrorCode.NOT_FOUND, "Không tìm thấy thông tin báo cáo"));

    if (!UserUtil.getCurrentUser().getUserId().equals(report.getUser().getId()))
      throw new JobFinderException(ResultCode.FORBIDDEN);

    jobReportRepository.deleteById(reportId);
  }
}
