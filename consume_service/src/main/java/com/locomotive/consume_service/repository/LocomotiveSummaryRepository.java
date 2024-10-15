package com.locomotive.consume_service.repository;

import com.locomotive.consume_service.model.LocomotiveSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface LocomotiveSummaryRepository extends JpaRepository<LocomotiveSummary, Long> {

    Optional<LocomotiveSummary> findByLocomotiveCodeAndTimestamp(@Param("locomotiveCode") String locomotiveCode, @Param("timestamp") LocalDateTime timestamp);

    @Query("SELECT ls FROM LocomotiveSummary ls WHERE ls.locomotiveCode = :locomotiveCode ORDER BY ls.timestamp DESC LIMIT 1")
    Optional<LocomotiveSummary> findLatestByLocomotiveCode(@Param("locomotiveCode") String locomotiveCode);

    @Query("SELECT ls FROM LocomotiveSummary ls ORDER BY ls.timestamp DESC Limit 1")
    Optional<LocomotiveSummary> findLatest();
}
