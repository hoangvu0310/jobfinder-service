package com.hoang.jobfinder.entity.company;

import com.hoang.jobfinder.common.Enum;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "company_draft")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDraft {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "draft_id", nullable = false)
  private Long draftId;

  @Column(name = "company_id")
  private Long companyId;

  @Column(name = "payload", columnDefinition = "jsonb")
  private String payload;

  @Column(name = "status", length = 20)
  @Enumerated(EnumType.STRING)
  private Enum.CreateEditStatus status;

  @Column(name = "action_type", length = 20)
  @Enumerated(EnumType.STRING)
  private Enum.ActionType actionType;

  @Column(name = "edited_by")
  private String postedBy;

  @Column(name = "edited_at")
  private Instant postedAt;

  @Column(name = "handled_by")
  private String handledBy;

  @Column(name = "handled_at")
  private Instant handledAt;

  @Column(name = "reject_reason")
  private String rejectReason;
}
