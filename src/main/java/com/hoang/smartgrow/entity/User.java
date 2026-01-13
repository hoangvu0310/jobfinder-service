package com.hoang.smartgrow.entity;

import com.hoang.smartgrow.common.Role;
import com.hoang.smartgrow.entity.base.BaseCreatedEntity;
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

  @Column(name = "role", nullable = false)
  private Role role;

  @Column(name = "email")
  private String email;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Builder.Default
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<RefreshToken> refreshTokens = new ArrayList<>();
}