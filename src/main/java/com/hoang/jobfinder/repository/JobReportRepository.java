package com.hoang.jobfinder.repository;

import com.hoang.jobfinder.entity.job.JobReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobReportRepository extends JpaRepository<JobReport, Long> {
}
