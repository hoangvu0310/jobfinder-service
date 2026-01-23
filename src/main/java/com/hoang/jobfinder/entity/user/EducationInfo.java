package com.hoang.jobfinder.entity.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "education_info")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EducationInfo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;

  @Column(name = "major", nullable = false)
  private String major;

  @Column(name = "facility", nullable = false)
  private String facility;

  @Column(name = "start_time", nullable = false)
  private Instant startTime;

  @Column(name = "end_time")
  private Instant endTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "profile_id")
  private UserProfile userProfile;
}
