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
@Table(name = "job_application")
@SuperBuilder
@NoArgsConstructor
public class JobApplication extends BaseCreatedEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "cv_key", nullable = false)
  private String cvKey;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "userId", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "jobId", nullable = false)
  private Job job;
}
