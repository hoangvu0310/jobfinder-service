package com.hoang.jobfinder.entity.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "work_experience")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkExperience {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "job_title", nullable = false)
  private String jobTitle;

  @Column(name = "company", nullable = false)
  private String company;

  @Column(name = "start_time", nullable = false)
  private Instant startTime;

  @Column(name = "end_time")
  private Instant endTime;

  @Column(name = "description", columnDefinition = "text")
  private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "profile_id")
  private UserProfile userProfile;
}
