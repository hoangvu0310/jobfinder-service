package com.hoang.jobfinder.specification;

import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.entity.job.JobDraft;
import org.springframework.data.jpa.domain.Specification;

public class JobDraftSpecification {
  public static Specification<JobDraft> hasActionType(Enum.ActionType actionType) {
    return (root, query, criteriaBuilder) ->
        actionType == null ? null : criteriaBuilder.equal(root.get("actionType"), actionType);
  }
}
