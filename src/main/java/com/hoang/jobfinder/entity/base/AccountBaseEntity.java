package com.hoang.jobfinder.entity.base;

import com.hoang.jobfinder.common.Enum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
public abstract class AccountBaseEntity extends BaseCreatedEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true)
  private Long id;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "password")
  private String password;

  @Column(name = "role", nullable = false, length = 10)
  @Enumerated(EnumType.STRING)
  private Enum.Role role;

  @Column(name = "auth_type", nullable = false, length = 30)
  @Enumerated(EnumType.STRING)
  private Enum.AuthType authType;
}
