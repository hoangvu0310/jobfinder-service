package com.hoang.jobfinder.repository;

import com.hoang.jobfinder.entity.user.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}
