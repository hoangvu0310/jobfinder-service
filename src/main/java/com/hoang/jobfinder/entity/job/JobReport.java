package com.hoang.jobfinder.entity.job;

import com.hoang.jobfinder.entity.base.BaseCreatedEntity;
import com.hoang.jobfinder.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@Table(name = "job_report")
@SuperBuilder
@NoArgsConstructor
public class JobReport extends BaseCreatedEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "report_id", nullable = false)
  private Long reportId;

  @Column(name = "reason", nullable = false)
  private String reason;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "job_id", nullable = false)
  private Job job;
}
