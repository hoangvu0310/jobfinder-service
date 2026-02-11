package com.hoang.jobfinder.entity.user;

import com.hoang.jobfinder.entity.base.BaseAuditableEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@Table(name = "cv")
@SuperBuilder
@NoArgsConstructor
public class CV extends BaseAuditableEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cv_id", nullable = false)
  private Long cvId;

  @Column(name = "cv_key", nullable = false)
  private String cvKey;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
}
