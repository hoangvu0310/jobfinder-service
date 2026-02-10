package com.hoang.jobfinder.service.impl;

import com.hoang.jobfinder.common.Const;
import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.common.ErrorCode;
import com.hoang.jobfinder.document.JobDocument;
import com.hoang.jobfinder.dto.PageableResponse;
import com.hoang.jobfinder.dto.PagingDTO;
import com.hoang.jobfinder.dto.auth.response.AccountInfoDTO;
import com.hoang.jobfinder.dto.company.request.RejectRequestDTO;
import com.hoang.jobfinder.dto.job.request.CreateEditJobRequestDTO;
import com.hoang.jobfinder.dto.job.response.JobDTO;
import com.hoang.jobfinder.dto.job.response.JobDraftDTO;
import com.hoang.jobfinder.entity.company.Company;
import com.hoang.jobfinder.entity.job.Job;
import com.hoang.jobfinder.entity.job.JobDraft;
import com.hoang.jobfinder.exception.JobFinderException;
import com.hoang.jobfinder.repository.CompanyRepository;
import com.hoang.jobfinder.repository.JobDraftRepository;
import com.hoang.jobfinder.repository.JobRepository;
import com.hoang.jobfinder.service.AdminJobService;
import com.hoang.jobfinder.service.MeiliSearchService;
import com.hoang.jobfinder.service.SupabaseS3Service;
import com.hoang.jobfinder.specification.JobDraftSpecification;
import com.hoang.jobfinder.util.CompanyUtil;
import com.hoang.jobfinder.util.UserUtil;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class AdminJobServiceImpl implements AdminJobService {
  private JobRepository jobRepository;

  private JobDraftRepository jobDraftRepository;

  private CompanyRepository companyRepository;

  private SupabaseS3Service supabaseS3Service;

  private MeiliSearchService meiliSearchService;

  private ModelMapper modelMapper;

  private ObjectMapper objectMapper;

  @Override
  public PageableResponse<JobDraftDTO> getAllJobDrafts(PagingDTO pagingDTO, Enum.ActionType actionType) {
    Pageable pageable = PageRequest.of(pagingDTO.getPageNumber(), pagingDTO.getPageSize());
    Specification<JobDraft> jobDraftSpecification = Specification.where(JobDraftSpecification.hasActionType(actionType));

    Page<JobDraft> jobDrafts = jobDraftRepository.findAll(jobDraftSpecification, pageable);
    Page<JobDraftDTO> jobDraftDTOS = jobDrafts.map(jobDraft -> {
      JobDraftDTO draftDTO = modelMapper.map(jobDraft, JobDraftDTO.class);
      draftDTO.setPayload(objectMapper.convertValue(jobDraft.getPayload(), CreateEditJobRequestDTO.class));

      return draftDTO;
    });

    return new PageableResponse<>(jobDraftDTOS);
  }

  @Override
  public JobDTO getJobRequestById(Long draftId) {
    JobDraft jobDraft = jobDraftRepository.findById(draftId)
        .orElseThrow(() -> new JobFinderException(ErrorCode.NOT_FOUND, "Không tìm thấy yêu cầu"));
    CreateEditJobRequestDTO createEditJobRequestDTO = objectMapper.convertValue(jobDraft.getPayload(), CreateEditJobRequestDTO.class);

    Company company = companyRepository.findById(jobDraft.getCompanyId())
        .orElseThrow(() -> new JobFinderException(ErrorCode.NOT_FOUND, "Không tìm thấy thông tin công ty"));

    return JobDTO.builder()
        .jobId(jobDraft.getJobId())
        .jobTitle(createEditJobRequestDTO.getJobTitle())
        .city(createEditJobRequestDTO.getCity())
        .minSalary(createEditJobRequestDTO.getMinSalary())
        .maxSalary(createEditJobRequestDTO.getMaxSalary())
        .description(createEditJobRequestDTO.getDescription())
        .requirement(createEditJobRequestDTO.getRequirement())
        .benefit(createEditJobRequestDTO.getBenefit())
        .dueDate(createEditJobRequestDTO.getDueDate())
        .workAddress(createEditJobRequestDTO.getWorkAddress())
        .employeeNeed(createEditJobRequestDTO.getEmployeeNeed())
        .experienceLevel(createEditJobRequestDTO.getExperienceLevel())
        .jobType(createEditJobRequestDTO.getJobType())
        .workplaceType(createEditJobRequestDTO.getWorkplaceType())
        .companyId(company.getCompanyId())
        .companyName(company.getCompanyName())
        .companyAvatarUrl(CompanyUtil.getAvatarUrl(company, supabaseS3Service))
        .build();
  }

  @Override
  @Transactional
  public JobDraftDTO approveCreateJob(Long draftId) {
    JobDraft jobDraft = jobDraftRepository.findById(draftId)
        .orElseThrow(() -> new JobFinderException(ErrorCode.NOT_FOUND, "Không tìm thấy yêu cầu"));
    CreateEditJobRequestDTO createEditJobRequestDTO = objectMapper.convertValue(jobDraft.getPayload(), CreateEditJobRequestDTO.class);

    Company company = companyRepository.findById(jobDraft.getCompanyId())
        .orElseThrow(() -> new JobFinderException(ErrorCode.NOT_FOUND, "Không tìm thấy thông tin công ty"));

    Job newJob = jobRepository.save(
        Job.builder()
            .jobTitle(createEditJobRequestDTO.getJobTitle())
            .city(createEditJobRequestDTO.getCity())
            .minSalary(createEditJobRequestDTO.getMinSalary())
            .maxSalary(createEditJobRequestDTO.getMaxSalary())
            .description(createEditJobRequestDTO.getDescription())
            .requirement(createEditJobRequestDTO.getRequirement())
            .benefit(createEditJobRequestDTO.getBenefit())
            .employeeNeed(createEditJobRequestDTO.getEmployeeNeed())
            .workAddress(createEditJobRequestDTO.getWorkAddress())
            .dueDate(createEditJobRequestDTO.getDueDate())
            .jobType(createEditJobRequestDTO.getJobType())
            .experienceLevel(createEditJobRequestDTO.getExperienceLevel())
            .workplaceType(createEditJobRequestDTO.getWorkplaceType())
            .postedAt(Instant.now())
            .company(company)
            .updatedBy(jobDraft.getPostedBy())
            .createdBy(jobDraft.getPostedBy())
            .build()
    );

    JobDocument document = JobDocument.builder()
        .id(newJob.getJobId())
        .jobTitle(newJob.getJobTitle())
        .city(newJob.getCity())
        .minSalary(newJob.getMinSalary())
        .maxSalary(newJob.getMaxSalary())
        .description(newJob.getDescription())
        .requirement(newJob.getRequirement())
        .benefit(newJob.getBenefit())
        .dueDateTimestamp(newJob.getDueDate().toEpochMilli())
        .postedAtTimestamp(newJob.getPostedAt().toEpochMilli())
        .jobType(newJob.getJobType())
        .experienceLevel(newJob.getExperienceLevel())
        .workplaceType(newJob.getWorkplaceType())
        .companyName(company.getCompanyName())
        .companyAvatarUrl(CompanyUtil.getAvatarUrl(company, supabaseS3Service))
        .build();

    meiliSearchService.indexDocument(Const.SearchIndexName.JOBS, document);

    return getJobDraftDTO(jobDraft, createEditJobRequestDTO);
  }

  @Override
  @Transactional
  public JobDraftDTO approveEditJob(Long draftId) {
    JobDraft jobDraft = jobDraftRepository.findById(draftId)
        .orElseThrow(() -> new JobFinderException(ErrorCode.NOT_FOUND, "Không tìm thấy yêu cầu"));
    CreateEditJobRequestDTO createEditJobRequestDTO = objectMapper.convertValue(jobDraft.getPayload(), CreateEditJobRequestDTO.class);

    Job job = jobRepository.findById(jobDraft.getCompanyId())
        .orElseThrow(() -> new JobFinderException(ErrorCode.NOT_FOUND, "Không tìm thấy thông tin công việc"));

    job.setJobTitle(createEditJobRequestDTO.getJobTitle());
    job.setCity(createEditJobRequestDTO.getCity());
    job.setMinSalary(createEditJobRequestDTO.getMinSalary());
    job.setMaxSalary(createEditJobRequestDTO.getMaxSalary());
    job.setDescription(createEditJobRequestDTO.getDescription());
    job.setRequirement(createEditJobRequestDTO.getRequirement());
    job.setBenefit(createEditJobRequestDTO.getBenefit());
    job.setEmployeeNeed(createEditJobRequestDTO.getEmployeeNeed());
    job.setWorkAddress(createEditJobRequestDTO.getWorkAddress());
    job.setJobType(createEditJobRequestDTO.getJobType());
    job.setExperienceLevel(createEditJobRequestDTO.getExperienceLevel());
    job.setWorkplaceType(createEditJobRequestDTO.getWorkplaceType());
    job.setDueDate(createEditJobRequestDTO.getDueDate());

    JobDocument jobDocument = meiliSearchService.getDocument(Const.SearchIndexName.JOBS, job.getJobId(), JobDocument.class);

    jobDocument.setJobTitle(createEditJobRequestDTO.getJobTitle());
    jobDocument.setCity(createEditJobRequestDTO.getCity());
    jobDocument.setMaxSalary(createEditJobRequestDTO.getMaxSalary());
    jobDocument.setDescription(createEditJobRequestDTO.getDescription());
    jobDocument.setRequirement(createEditJobRequestDTO.getRequirement());
    jobDocument.setBenefit(createEditJobRequestDTO.getBenefit());
    jobDocument.setJobType(createEditJobRequestDTO.getJobType());
    jobDocument.setExperienceLevel(createEditJobRequestDTO.getExperienceLevel());
    jobDocument.setWorkplaceType(createEditJobRequestDTO.getWorkplaceType());
    jobDocument.setDueDateTimestamp(createEditJobRequestDTO.getDueDate().toEpochMilli());

    meiliSearchService.updateDocuments(Const.SearchIndexName.JOBS, List.of(jobDocument));

    return getJobDraftDTO(jobDraft, createEditJobRequestDTO);
  }

  @Override
  @Transactional
  public JobDraftDTO rejectJob(RejectRequestDTO rejectRequestDTO) {
    JobDraft jobDraft = jobDraftRepository.findById(rejectRequestDTO.getDraftId())
        .orElseThrow(() -> new JobFinderException(ErrorCode.NOT_FOUND, "Không tìm thấy yêu cầu"));
    CreateEditJobRequestDTO createEditJobRequestDTO = objectMapper.convertValue(jobDraft.getPayload(), CreateEditJobRequestDTO.class);

    AccountInfoDTO adminInfo = UserUtil.getCurrentUser();
    jobDraft.setStatus(Enum.CreateEditStatus.APPROVED);
    jobDraft.setHandledAt(Instant.now());
    jobDraft.setHandledBy(adminInfo.getEmail());
    jobDraft.setRejectReason(rejectRequestDTO.getRejectReason());

    JobDraftDTO jobDraftDTO = modelMapper.map(jobDraft, JobDraftDTO.class);
    jobDraftDTO.setPayload(createEditJobRequestDTO);
    return jobDraftDTO;
  }

  @NonNull
  private JobDraftDTO getJobDraftDTO(JobDraft jobDraft, CreateEditJobRequestDTO createEditJobRequestDTO) {
    AccountInfoDTO adminInfo = UserUtil.getCurrentUser();
    jobDraft.setStatus(Enum.CreateEditStatus.APPROVED);
    jobDraft.setHandledAt(Instant.now());
    jobDraft.setHandledBy(adminInfo.getEmail());

    JobDraftDTO jobDraftDTO = modelMapper.map(jobDraft, JobDraftDTO.class);
    jobDraftDTO.setPayload(createEditJobRequestDTO);
    return jobDraftDTO;
  }
}
