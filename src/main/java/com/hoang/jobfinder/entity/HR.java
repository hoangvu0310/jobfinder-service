package com.hoang.jobfinder.entity;

import com.hoang.jobfinder.entity.base.AccountBaseEntity;
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
@Table(name = "hr")
@SuperBuilder
@NoArgsConstructor
public class HR extends AccountBaseEntity {
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "company_id")
  private Company company;

  @Builder.Default
  @OneToMany(mappedBy = "hr", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<RefreshToken> refreshTokens = new ArrayList<>();
}
