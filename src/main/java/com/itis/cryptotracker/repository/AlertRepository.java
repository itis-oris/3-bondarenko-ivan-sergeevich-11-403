package com.itis.cryptotracker.repository;

import com.itis.cryptotracker.model.Alert;
import com.itis.cryptotracker.model.enums.AlertStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface AlertRepository extends JpaRepository<Alert, Long>, AlertRepositoryCustom {

    @EntityGraph(attributePaths = "coin")
    List<Alert> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    @EntityGraph(attributePaths = "coin")
    Optional<Alert> findByIdAndUserId(Long id, Long userId);

    long countByUserIdAndStatus(Long userId, AlertStatus status);

    @Modifying
    @Query("delete from Alert a where a.coin.id = :coinId")
    void deleteAllByCoinId(@Param("coinId") Long coinId);

    @Query("""
            select a from Alert a
            join fetch a.coin
            where a.user.id = :userId
              and (
                select count(n.id) from Notification n
                where n.alert = a and n.createdAt > :since
              ) >= :minTriggers
            order by a.createdAt desc
            """)
    List<Alert> findFrequentlyTriggered(@Param("userId") Long userId,
                                        @Param("since") Instant since,
                                        @Param("minTriggers") long minTriggers);
}
