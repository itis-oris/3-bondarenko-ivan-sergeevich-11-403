package com.itis.cryptotracker.repository;

import com.itis.cryptotracker.model.WatchlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WatchlistItemRepository extends JpaRepository<WatchlistItem, Long> {

    List<WatchlistItem> findAllByUserIdOrderByAddedAtDesc(Long userId);

    Optional<WatchlistItem> findByUserIdAndCoinId(Long userId, Long coinId);

    boolean existsByUserIdAndCoinId(Long userId, Long coinId);

    void deleteByUserIdAndCoinId(Long userId, Long coinId);

    @Modifying
    @Query("delete from WatchlistItem w where w.coin.id = :coinId")
    void deleteAllByCoinId(@Param("coinId") Long coinId);
}
