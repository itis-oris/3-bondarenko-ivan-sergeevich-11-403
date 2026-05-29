package com.itis.cryptotracker.model;

import com.itis.cryptotracker.model.enums.AlertDirection;
import com.itis.cryptotracker.model.enums.AlertStatus;
import com.itis.cryptotracker.model.enums.NotificationChannel;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
        name = "alerts",
        indexes = {
                @Index(name = "idx_alerts_status", columnList = "status"),
                @Index(name = "idx_alerts_user", columnList = "user_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "direction", "targetPrice", "status"})
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coin_id", nullable = false)
    private Coin coin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private AlertDirection direction;

    @Column(name = "target_price", nullable = false, precision = 24, scale = 8)
    private BigDecimal targetPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private NotificationChannel channel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private AlertStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "triggered_at")
    private Instant triggeredAt;

    @Column(length = 256)
    private String comment;

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (status == null) {
            status = AlertStatus.ACTIVE;
        }
    }
}
