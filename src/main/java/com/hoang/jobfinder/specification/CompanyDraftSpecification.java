package com.hoang.jobfinder.specification;

import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.entity.company.CompanyDraft;
import org.springframework.data.jpa.domain.Specification;

public class CompanyDraftSpecification {
  public static Specification<CompanyDraft> hasActionType(Enum.ActionType actionType) {
    return ((root, query, criteriaBuilder) ->
        actionType == null ? null : criteriaBuilder.equal(root.get("actionType"), actionType)
    );
  }
}
