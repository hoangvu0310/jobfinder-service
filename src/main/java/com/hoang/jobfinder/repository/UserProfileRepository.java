package com.hoang.jobfinder.repository;

import com.hoang.jobfinder.entity.user.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
  @Query("select u.userProfile from User u where u.id = :userId")
  UserProfile findUserProfileByUserId(@Param("userId") Long userId);
}
