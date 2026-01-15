package com.hoang.jobfinder.config.security;

import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Getter
public class PasswordEncoderService {

  private final PasswordEncoder encoder = new BCryptPasswordEncoder();

  public String encodePassword(String password) {
    return encoder.encode(password);
  }

  public Boolean matches(String password, String hashedPassword) {
    return encoder.matches(password, hashedPassword);
  }
}
