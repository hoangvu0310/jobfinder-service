package com.hoang.jobfinder.entity.company;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
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
