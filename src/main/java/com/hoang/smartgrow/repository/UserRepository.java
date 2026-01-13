package com.hoang.smartgrow.repository;

import com.hoang.smartgrow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findUserByUsername(String username);

  User findUserByUserId(Long userId);
}