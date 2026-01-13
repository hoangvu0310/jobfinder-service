package com.hoang.smartgrow.repository;

import com.hoang.smartgrow.entity.RefreshToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
  @Query("select r from RefreshToken r where r.user.userId = :userId")
  Optional<RefreshToken> findRefreshTokenByUserId(@Param("userId") Long userId);

  @Modifying
  @Query("delete from RefreshToken r where r.user.userId = :userId")
  void deleteRefreshTokenByUserId(@Param("userId") Long userId);


}