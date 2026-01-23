package com.hoang.jobfinder.repository;

import com.hoang.jobfinder.entity.RefreshToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
  @Query("select r from RefreshToken r where (:isHR = false and r.user.id = :id) or (:isHR and r.hr.id = :id)")
  List<RefreshToken> findRefreshTokenByUserId(@Param("id") Long userId, @Param("isHR") Boolean isHR);
  @Modifying
  @Query("delete from RefreshToken r where (:isHR = false and r.user.id = :id) or (:isHR and r.hr.id = :id)")
  void deleteRefreshTokenByUserId(@Param("id") Long id, @Param("isHR") Boolean isHR);
}