package com.itis.cryptotracker.repository;

import com.itis.cryptotracker.model.PortfolioEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PortfolioEntryRepository extends JpaRepository<PortfolioEntry, Long> {

    List<PortfolioEntry> findAllByUserIdOrderByBoughtAtDesc(Long userId);

    Optional<PortfolioEntry> findByIdAndUserId(Long id, Long userId);

    @Modifying
    @Query("delete from PortfolioEntry p where p.coin.id = :coinId")
    void deleteAllByCoinId(@Param("coinId") Long coinId);
}
