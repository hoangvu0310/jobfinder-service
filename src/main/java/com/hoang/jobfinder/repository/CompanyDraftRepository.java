package com.hoang.jobfinder.repository;

import com.hoang.jobfinder.entity.company.CompanyDraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyDraftRepository extends JpaRepository<CompanyDraft, Long>, JpaSpecificationExecutor<CompanyDraft> {
  CompanyDraft findCompanyDraftByDraftId(Long draftId);
}
