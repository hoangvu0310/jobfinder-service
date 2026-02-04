package com.hoang.jobfinder.entity.job;

import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.entity.base.BaseAuditableEntity;
import com.hoang.jobfinder.entity.company.Company;
import com.hoang.jobfinder.entity.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "job")
@SuperBuilder
@NoArgsConstructor
public class Job extends BaseAuditableEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "job_id", nullable = false)
  private Long jobId;

  @Column(name = "job_status", length = 20)
  @Enumerated(EnumType.STRING)
  private Enum.JobStatus jobStatus;

  @Column(name = "job_title", nullable = false, length = 100)
  private String jobTitle;

  @Column(name = "city", nullable = false, length = 100)
  private String city;

  @Column(name = "min_salary")
  private Integer minSalary;

  @Column(name = "max_salary")
  private Integer maxSalary;

  @Column(name = "description", columnDefinition = "text", nullable = false)
  private String description;

  @Column(name = "requirement", columnDefinition = "text", nullable = false)
  private String requirement;

  @Column(name = "benefit", columnDefinition = "text", nullable = false)
  private String benefit;

  @Column(name = "employee_need", nullable = false)
  private Integer employeeNeed;

  @Column(name = "work_address", nullable = false)
  private String workAddress;

  @Column(name = "due_date")
  private LocalDate dueDate;

  @Column(name = "job_type", length = 20)
  @Enumerated(EnumType.STRING)
  private Enum.JobType jobType;

  @Column(name = "experience_level", length = 20)
  @Enumerated(EnumType.STRING)
  private Enum.ExperienceLevel experienceLevel;

  @Column(name = "workplace_type", length = 20)
  @Enumerated(EnumType.STRING)
  private Enum.WorkplaceType workplaceType;

  @Column(name = "posted_at", nullable = false)
  private LocalDate postedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "company_id")
  private Company company;

  @Builder.Default
  @ManyToMany
  @JoinTable(
      name = "job_applicant",
      joinColumns = @JoinColumn(name = "job_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id")
  )
  private Set<User> applicantList = new HashSet<>();
}
