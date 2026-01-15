package com.hoang.jobfinder.repository;

import com.hoang.jobfinder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findUserByUsername(String username);
  User findUserByUserId(Long userId);
  boolean existsUserByUsername(String username);

  void deleteUserByUserId(Long userId);
}