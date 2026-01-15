package com.hoang.jobfinder.entity;

import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.entity.base.BaseAuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "refresh_token")
@SuperBuilder
@NoArgsConstructor
public class RefreshToken extends BaseAuditableEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "refresh_token_id", nullable = false)
  private Long refreshTokenId;

  @Column(name = "token", nullable = false, unique = true)
  private String token;

  @Column(name = "expiration_date", nullable = false)
  private Instant expirationDate;

  @Column(name = "device_id", nullable = false)
  private String deviceId;

  /**
   * 0: Android
   * 1: Ios
   */
  @Column(name = "platform", length = 10)
  @Enumerated(EnumType.STRING)
  private Enum.Platform platform;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
}
