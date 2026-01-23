package com.hoang.jobfinder.repository;

import com.hoang.jobfinder.entity.HR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HRRepository extends JpaRepository<HR, Long> {
  boolean existsHRByEmail(String email);
  HR findHRById(Long id);
  void deleteHRById(Long id);
  @Modifying
  @Query("select h from HR h where h.company.companyId = :companyId")
  List<HR> findHRByCompanyId(@Param("companyId") Long companyId);
  Optional<HR> findHRByEmail(String email);
}
