package com.hoang.jobfinder.repository;

import com.hoang.jobfinder.entity.job.JobDraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobDraftRepository extends JpaRepository<JobDraft, Long>{
}
