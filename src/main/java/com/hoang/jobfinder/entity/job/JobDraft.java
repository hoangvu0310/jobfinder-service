package com.hoang.jobfinder.entity.job;

import com.hoang.jobfinder.common.Enum;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "job_draft")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobDraft {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "draft_id", nullable = false)
  private Long draftId;

  @Column(name = "job_id")
  private Long jobId;

  @Column(name = "request_description", nullable = false)
  private String requestDescription;

  @Column(name = "payload", columnDefinition = "jsonb")
  private String payload;

  @Column(name = "create_edit_status", length = 20)
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
