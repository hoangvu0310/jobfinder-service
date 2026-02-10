package com.hoang.jobfinder.service.impl;

import com.hoang.jobfinder.common.Const;
import com.hoang.jobfinder.common.ErrorCode;
import com.hoang.jobfinder.dto.PageableResponse;
import com.hoang.jobfinder.dto.PagingDTO;
import com.hoang.jobfinder.dto.job.request.JobFilterDTO;
import com.hoang.jobfinder.dto.job.response.JobDTO;
import com.hoang.jobfinder.dto.job.response.JobPreviewDTO;
import com.hoang.jobfinder.entity.company.Company;
import com.hoang.jobfinder.entity.job.Job;
import com.hoang.jobfinder.entity.user.User;
import com.hoang.jobfinder.exception.JobFinderException;
import com.hoang.jobfinder.repository.JobRepository;
import com.hoang.jobfinder.repository.UserRepository;
import com.hoang.jobfinder.service.JobService;
import com.hoang.jobfinder.service.MeiliSearchService;
import com.hoang.jobfinder.service.SupabaseS3Service;
import com.hoang.jobfinder.util.CompanyUtil;
import com.hoang.jobfinder.util.UserUtil;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class JobServiceImpl implements JobService {
  private JobRepository jobRepository;

  private UserRepository userRepository;

  private ModelMapper modelMapper;

  private SupabaseS3Service supabaseS3Service;

  private MeiliSearchService meiliSearchService;

  @PostConstruct
  private void initJobIndex() {
    log.info("Initialize job index with meilisearch");

    meiliSearchService.initializeIndex(
        Const.SearchIndexName.JOBS,
        new String[] {"jobTitle", "description", "requirement", "benefit", "city", "experienceLevel", "companyName"},
        new String[] {"city", "minSalary", "maxSalary", "experienceLevel", "workplaceType", "jobType"},
        new String[] {"postedAtTimeStamp"},
        null
    );

    log.info("Initialize done for job index with meilisearch");

  }

  @Override
  public PageableResponse<JobPreviewDTO> findJob(JobFilterDTO filterDTO, PagingDTO pagingDTO) {
    Function<HashMap<String, Object>, JobPreviewDTO> mapper =
        document -> JobPreviewDTO.builder()
            .jobId(Long.valueOf(document.get("jobId").toString()))
            .jobTitle(document.get("jobTitle").toString())
            .city(document.get("jobTitle").toString())
            .minSalary(Integer.valueOf(document.get("minSalary").toString()))
            .maxSalary(Integer.valueOf(document.get("maxSalary").toString()))
            .postedAt(Instant.parse(document.get("postedAtTimestamp").toString()))
            .companyName(document.get("companyName").toString())
            .companyAvatarUrl(document.get("companyAvatarUrl").toString())
            .build();

    String[] sort = new String[] {"postedAtTimeStamp:desc"};

    return meiliSearchService.search(
        Const.SearchIndexName.JOBS,
        filterDTO.getSearchText(),
        pagingDTO,
        buildFilter(filterDTO),
        sort,
        mapper
    );
  }

  @Override
  public JobDTO findJobById(Long jobId) {
    Job job = jobRepository.findById(jobId)
        .orElseThrow(() -> new JobFinderException(ErrorCode.NOT_FOUND, "Không tìm thấy thông tin công việc"));

    Company company = job.getCompany();
    JobDTO jobDTO = modelMapper.map(job, JobDTO.class);
    jobDTO.setCompanyId(company.getCompanyId());
    jobDTO.setCompanyName(company.getCompanyName());
    jobDTO.setCompanyAvatarUrl(CompanyUtil.getAvatarUrl(company,supabaseS3Service));

    return jobDTO;
  }

  @Override
  public void applyJob(Long jobId) {
    Job job = jobRepository.findById(jobId)
        .orElseThrow(() -> new JobFinderException(ErrorCode.NOT_FOUND, "Không tìm thấy thông tin công việc"));

    Set<User> applicantList = job.getApplicantList();
    User user = userRepository.findById(UserUtil.getCurrentUser().getUserId())
        .orElseThrow(() -> new JobFinderException(ErrorCode.NOT_FOUND, "Không tìm thấy thông tin người dùng"));
    applicantList.add(user);
  }

  @Override
  public void reportJob() {

  }

  private String[] buildFilter(JobFilterDTO filterDTO) {
    List<String> filters = new ArrayList<>();

    if (filterDTO.getCity() != null && !filterDTO.getCity().trim().isEmpty()) {
      filters.add("city = '" + filterDTO.getCity() + "'");
    }

    if (filterDTO.getMinSalary() != null) {
      filters.add("minSalary >= " + filterDTO.getMinSalary());
    }

    if (filterDTO.getMaxSalary() != null) {
      filters.add("maxSalary <= " + filterDTO.getMaxSalary());
    }

    if (filterDTO.getExperienceLevel() != null) {
      filters.add("experienceLevel = '" + filterDTO.getExperienceLevel() + "'");
    }

    if (filterDTO.getJobType() != null) {
      filters.add("jobType = '" + filterDTO.getJobType() + "'");
    }

    if (filterDTO.getWorkplaceType() != null) {
      filters.add("workplaceType = '" + filterDTO.getWorkplaceType() + "'");
    }

    return new String[] {
        String.join(" AND ", filters)
    };
  }
 }
