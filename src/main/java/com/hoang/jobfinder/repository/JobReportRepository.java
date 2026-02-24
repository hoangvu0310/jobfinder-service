package com.hoang.jobfinder.repository;

import com.hoang.jobfinder.dto.job.response.JobReportDTO;
import com.hoang.jobfinder.entity.job.JobReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobReportRepository extends JpaRepository<JobReport, Long> {
  @Query("""
        select new com.hoang.jobfinder.dto.job.response.JobReportDTO(
            u.id,
            up.avatarUrlKey,
            up.fullName,
            rp.reason,
            rp.createdAt
        )
        from JobReport rp
        join rp.user u
        join u.userProfile up
        where rp.job.jobId = :jobId
        order by rp.createdAt desc
        """)
  List<JobReportDTO> findReportsByJobId(@Param("jobId") Long jobId);
}
