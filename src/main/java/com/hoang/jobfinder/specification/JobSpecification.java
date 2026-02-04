package com.hoang.jobfinder.specification;

import com.hoang.jobfinder.dto.job.request.JobFilterDTO;
import com.hoang.jobfinder.entity.job.Job;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class JobSpecification {
  public static Specification<Job> filterJob(JobFilterDTO filterDTO) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (filterDTO.getCity() != null) {
        predicates.add(criteriaBuilder.equal(root.get("city"), filterDTO.getCity()));
      }

      if (filterDTO.getMinSalary() != null) {
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("minSalary"), filterDTO.getMinSalary()));
      }

      if (filterDTO.getMaxSalary() != null) {
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("maxSalary"), filterDTO.getMaxSalary()));
      }

      if (filterDTO.getExperienceLevel() != null) {
        predicates.add(criteriaBuilder.equal(root.get("experienceLevel"), filterDTO.getExperienceLevel()));
      }

      if (filterDTO.getJobType() != null) {
        predicates.add(criteriaBuilder.equal(root.get("jobType"), filterDTO.getJobType()));
      }

      if (filterDTO.getJobStatus() != null) {
        predicates.add(criteriaBuilder.equal(root.get("jobStatus"), filterDTO.getJobStatus()));
      }

      if (filterDTO.getWorkplaceType() != null) {
        predicates.add(criteriaBuilder.equal(root.get("workplaceType"), filterDTO.getWorkplaceType()));
      }

      return criteriaBuilder.and(predicates);
    };
  }

  public static Specification<Job> fetchCompany() {
    return (root, query, criteriaBuilder) -> {
      if (query.getResultType() != Long.class &&
          query.getResultType() != long.class) {
        root.fetch("company", JoinType.LEFT);
      }
      return null;
    };
  }

  public static Specification<Job> hasCompanyId(Long companyId) {
    return (root, query, criteriaBuilder) ->
        companyId == null ? null : criteriaBuilder.equal(root.get("company").get("companyId"), companyId);
  }
}
