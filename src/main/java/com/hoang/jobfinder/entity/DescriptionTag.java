package com.hoang.jobfinder.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "description_tag")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DescriptionTag {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long tagId;

  @Column(name = "title", unique = true)
  private String title;

  @Column(name = "slug", unique = true)
  private String slug;
}
