package com.itis.cryptotracker.repository;

import com.itis.cryptotracker.model.PriceSnapshot;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface PriceSnapshotRepository extends JpaRepository<PriceSnapshot, Long> {

    List<PriceSnapshot> findAllByCoinIdOrderByRecordedAtDesc(Long coinId, Pageable pageable);

    @Modifying
    @Query("delete from PriceSnapshot p where p.coin.id = :coinId")
    void deleteAllByCoinId(@Param("coinId") Long coinId);

    @Modifying
    @Query("delete from PriceSnapshot p where p.recordedAt <= :instant")
    int deleteOlderThan(@Param("instant") Instant instant);
}
