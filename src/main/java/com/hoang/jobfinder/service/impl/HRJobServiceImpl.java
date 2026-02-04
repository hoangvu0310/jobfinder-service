package com.hoang.jobfinder.service.impl;

import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.common.ErrorCode;
import com.hoang.jobfinder.dto.PageableResponse;
import com.hoang.jobfinder.dto.PagingDTO;
import com.hoang.jobfinder.dto.auth.response.AccountInfoDTO;
import com.hoang.jobfinder.dto.job.request.CreateEditJobRequestDTO;
import com.hoang.jobfinder.dto.job.request.JobFilterDTO;
import com.hoang.jobfinder.dto.job.response.JobDTO;
import com.hoang.jobfinder.dto.job.response.JobDraftDTO;
import com.hoang.jobfinder.dto.job.response.JobPreviewDTO;
import com.hoang.jobfinder.entity.HR;
import com.hoang.jobfinder.entity.company.Company;
import com.hoang.jobfinder.entity.job.JobDraft;
import com.hoang.jobfinder.exception.JobFinderException;
import com.hoang.jobfinder.repository.HRRepository;
import com.hoang.jobfinder.repository.JobDraftRepository;
import com.hoang.jobfinder.repository.JobRepository;
import com.hoang.jobfinder.service.HRJobService;
import com.hoang.jobfinder.service.SupabaseS3Service;
import com.hoang.jobfinder.util.UserUtil;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class HRJobServiceImpl implements HRJobService {
  private JobRepository jobRepository;

  private JobDraftRepository jobDraftRepository;

  private HRRepository hrRepository;

  private SupabaseS3Service supabaseS3Service;

  private ObjectMapper objectMapper;

  private ModelMapper modelMapper;

  @Override
  public PageableResponse<JobPreviewDTO> findJobsByCompany(JobFilterDTO filterDTO, PagingDTO pagingDTO) {
    AccountInfoDTO hrInfo = UserUtil.getCurrentUser();
    HR hr = hrRepository.findHRById(hrInfo.getUserId());
    Company company = hr.getCompany();
    if (company == null) {
      throw new JobFinderException(ErrorCode.NOT_FOUND, "Không tìm thấy thông tin công ty");
    }

    Pageable pageable = PageRequest.of(pagingDTO.getPageNumber(), pagingDTO.getPageSize());
    Page<JobPreviewDTO> jobPreviewDTOS = jobRepository.findJobsByCompanyId(
        pageable,
        company.getCompanyId(),
        filterDTO.getCity(),
        filterDTO.getMinSalary(),
        filterDTO.getMaxSalary(),
        filterDTO.getExperienceLevel(),
        filterDTO.getJobType(),
        filterDTO.getWorkplaceType()
    );
    jobPreviewDTOS.forEach(jobPreviewDTO -> {
      // set to url from key get from db
      String avatarKey = jobPreviewDTO.getCompanyAvatarUrl();
      jobPreviewDTO.setCompanyAvatarUrl(supabaseS3Service.generatePublicGetUrl(avatarKey));
    });

    return new PageableResponse<>(jobPreviewDTOS);
  }

  @Override
  public JobDTO getJobInfo(Long jobId) {
    JobDTO jobDTO = jobRepository.getJobInfo(jobId)
        .orElseThrow(() -> new JobFinderException(ErrorCode.NOT_FOUND, "Không tìm thấy thông tin công việc"));

    // set to url from key get from db
    String avatarKey = jobDTO.getCompanyAvatarUrl();
    jobDTO.setCompanyAvatarUrl(supabaseS3Service.generatePublicGetUrl(avatarKey));

    return jobDTO;
  }

  @Override
  @Transactional
  public JobDraftDTO uploadJob(CreateEditJobRequestDTO requestDTO) {
    AccountInfoDTO hrInfo = UserUtil.getCurrentUser();
    JobDraft newJobDraft = jobDraftRepository.save(
        JobDraft.builder()
            .requestDescription(requestDTO.getRequestDescription())
            .companyId(hrRepository.findHRById(hrInfo.getUserId()).getCompany().getCompanyId())
            .payload(objectMapper.writeValueAsString(requestDTO))
            .status(Enum.CreateEditStatus.PENDING)
            .actionType(Enum.ActionType.CREATE)
            .postedBy(hrInfo.getEmail())
            .postedAt(Instant.now())
            .build()
    );

    JobDraftDTO draftDTO = modelMapper.map(newJobDraft, JobDraftDTO.class);
    draftDTO.setPayload(requestDTO);

    return draftDTO;
  }

  @Override
  @Transactional
  public JobDraftDTO editJob(CreateEditJobRequestDTO requestDTO, Long jobId) {
    AccountInfoDTO hrInfo = UserUtil.getCurrentUser();
    JobDraft newJobDraft = jobDraftRepository.save(
        JobDraft.builder()
            .jobId(jobId)
            .companyId(hrRepository.findHRById(hrInfo.getUserId()).getCompany().getCompanyId())
            .requestDescription(requestDTO.getRequestDescription())
            .payload(objectMapper.writeValueAsString(requestDTO))
            .status(Enum.CreateEditStatus.PENDING)
            .actionType(Enum.ActionType.EDIT)
            .postedBy(hrInfo.getEmail())
            .postedAt(Instant.now())
            .build()
    );

    JobDraftDTO draftDTO = modelMapper.map(newJobDraft, JobDraftDTO.class);
    draftDTO.setPayload(requestDTO);

    return draftDTO;
  }

  @Override
  @Transactional
  public void deleteJob(Long jobId) {
    jobRepository.deleteById(jobId);
  }
}
