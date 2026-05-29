package com.itis.cryptotracker.repository;

import com.itis.cryptotracker.model.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @EntityGraph(attributePaths = {"alert", "alert.coin"})
    List<Notification> findAllByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    @Modifying
    @Query("delete from Notification n where n.alert.coin.id = :coinId")
    void deleteAllByAlertCoinId(@Param("coinId") Long coinId);

    long countByUserIdAndIsReadFalse(Long userId);

    @Modifying
    @Query("update Notification n set n.isRead = true where n.user.id = :userId and n.isRead = false")
    int markAllReadForUser(@Param("userId") Long userId);

    void deleteAllByAlert_Id(Long alertId);

    @Query("""
            select case when count(n) > 0 then true else false end
            from Notification n
            where n.alert.id = :alertId and n.createdAt > :since
            """)
    boolean existsForAlertSince(@Param("alertId") Long alertId, @Param("since") Instant since);

    long countByAlertId(Long alertId);
}
