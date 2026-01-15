package com.hoang.jobfinder.entity;

import com.hoang.jobfinder.common.Enum;
import com.hoang.jobfinder.entity.base.BaseCreatedEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user")
@SuperBuilder
@NoArgsConstructor
public class User extends BaseCreatedEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long userId;

  @Column(name = "username", nullable = false, unique = true)
  private String username;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "full_name", nullable = false)
  private String fullName;


  /**
   * 0: Role Admin
   * 1: Role Employer
   * 2: Role Employee
   */
  @Column(name = "role", nullable = false, length = 10)
  @Enumerated(EnumType.STRING)
  private Enum.Role role;

  @Column(name = "email")
  private String email;

  @Column(name = "phone_number", length = 20)
  private String phoneNumber;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<RefreshToken> refreshTokens;
}