package com.hoang.jobfinder.config.audit;

import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component(value = "securityAuditorAware")
public class SecurityAuditorAware implements AuditorAware<String> {
  @Override
  @NullMarked
  public Optional<String> getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null ||
        !authentication.isAuthenticated() ||
        Objects.equals(authentication.getPrincipal(), "anonymousUser")) {
      return Optional.empty();
    }

    if (authentication.getPrincipal() instanceof String) {
      return Optional.of((String) authentication.getPrincipal());
    }

    return Optional.empty();
  }
}
