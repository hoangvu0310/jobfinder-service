package com.hoang.jobfinder.entity.company;

import com.hoang.jobfinder.entity.HR;
import com.hoang.jobfinder.entity.job.Job;
import com.hoang.jobfinder.entity.base.BaseAuditableEntity;
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
@Table(name = "company")
@SuperBuilder
@NoArgsConstructor
public class Company extends BaseAuditableEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "company_id")
  private Long companyId;

  @Column(name = "company_name", length = 100, unique = true)
  private String companyName;

  @Column(name = "address", length = 100)
  private String address;

  @Column(name = "latitude")
  private Double latitude;

  @Column(name = "longitude")
  private Double longitude;

  @Column(name = "description", columnDefinition = "text")
  private String description;

  @Column(name = "website_url", length = 100)
  private String websiteUrl;

  @Column(name = "company_size")
  private Integer companySize;

  @Builder.Default
  @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CompanyAsset> companyAssets = new ArrayList<>();

  @Builder.Default
  @ManyToMany
  @JoinTable(
      name = "company_tag",
      joinColumns = @JoinColumn(name = "company_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id")
  )
  private Set<DescriptionTag> tags = new HashSet<>();

  @Builder.Default
  @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<HR> hrList = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Job> jobList = new ArrayList<>();
}
