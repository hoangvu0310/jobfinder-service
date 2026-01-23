package com.hoang.jobfinder.entity.user;

import com.hoang.jobfinder.entity.base.BaseAuditableEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user_profile")
@SuperBuilder
@NoArgsConstructor
public class UserProfile extends BaseAuditableEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "profile_id", nullable = false)
  private Long profileId;

  @Column(name = "full_name", nullable = false)
  private String fullName;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "address")
  private String address;

  @Column(name = "description", columnDefinition = "text")
  private String description;

  @Builder.Default
  @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<WorkExperience> workExperienceList = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<EducationInfo> educationInfoList = new ArrayList<>();
}