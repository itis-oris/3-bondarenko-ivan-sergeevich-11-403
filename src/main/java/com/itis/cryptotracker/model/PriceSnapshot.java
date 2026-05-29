package com.itis.cryptotracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
        name = "price_snapshots",
        indexes = @Index(name = "idx_snapshots_coin_time", columnList = "coin_id, recorded_at")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "priceUsd", "recordedAt"})
public class PriceSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coin_id", nullable = false)
    private Coin coin;

    @Column(name = "price_usd", nullable = false, precision = 24, scale = 8)
    private BigDecimal priceUsd;

    @Column(name = "recorded_at", nullable = false)
    private Instant recordedAt;
}
