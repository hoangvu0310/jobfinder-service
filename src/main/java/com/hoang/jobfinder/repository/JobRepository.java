package com.hoang.jobfinder.repository;

import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.dto.job.response.JobDTO;
import com.hoang.jobfinder.dto.job.response.JobPreviewDTO;
import com.hoang.jobfinder.entity.job.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {
  @Query("""
        select new com.hoang.jobfinder.dto.job.response.JobPreviewDTO(
            j.jobId,
            j.jobTitle,
            j.city,
            j.minSalary,
            j.maxSalary,
            j.postedAt,
            c.companyName,
            ca.assetKey
        )
        from Job j
        join j.company c
        left join c.companyAssets ca
            on ca.assetType = com.hoang.jobfinder.common.Enum.CompanyAssetType.AVATAR
        where c.companyId = :companyId
        and (:city is null or j.city = :city)
        and (:minSalary is null or j.minSalary >= :minSalary)
        and (:maxSalary is null or j.maxSalary <= :maxSalary)
        and (:experienceLevel is null or j.experienceLevel = :experienceLevel)
        and (:jobType is null or j.jobType = :jobType)
        and (:workplaceType is null or j.workplaceType = :workplaceType)
      """)
  Page<JobPreviewDTO> findJobsByCompanyId(
      Pageable pageable,
      @Param("companyId") Long companyId,
      @Param("city") String city,
      @Param("minSalary") Integer minSalary,
      @Param("maxSalary") Integer maxSalary,
      @Param("experienceLevel") Enum.ExperienceLevel experienceLevel,
      @Param("jobType") Enum.JobType jobType,
      @Param("workplaceType") Enum.WorkplaceType workplaceType
  );

  @Query("""
        select new com.hoang.jobfinder.dto.job.response.JobDTO(
            j.jobId,
            j.jobTitle,
            j.city,
            j.minSalary,
            j.maxSalary,
            j.description,
            j.requirement,
            j.benefit,
            j.dueDate,
            j.postedAt,
            j.workAddress,
            j.employeeNeed,
            j.experienceLevel,
            j.jobType,
            j.workplaceType,
            c.companyId,
            c.companyName,
            ca.assetKey
        )
        from Job j
        join j.company c
        left join c.companyAssets ca
            on ca.assetType = 'AVATAR'
        where j.jobId = :jobId
      """)
  Optional<JobDTO> getJobInfo(@Param("jobId") Long jobId);
}
