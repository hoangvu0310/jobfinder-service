package com.hoang.jobfinder.entity.user;

import com.hoang.jobfinder.entity.RefreshToken;
import com.hoang.jobfinder.entity.base.AccountBaseEntity;
import com.hoang.jobfinder.entity.job.JobApplication;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "user")
@SuperBuilder
@NoArgsConstructor
public class User extends AccountBaseEntity {
  @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "profile_id")
  private UserProfile userProfile;

  @Builder.Default
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<RefreshToken> refreshTokens = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<JobApplication> jobApplications = new HashSet<>();

  @Builder.Default
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<CV> cvSet = new HashSet<>();
}