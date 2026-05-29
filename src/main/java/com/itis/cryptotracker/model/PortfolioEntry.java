package com.itis.cryptotracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
        name = "portfolio_entries",
        indexes = @Index(name = "idx_portfolio_user", columnList = "user_id")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "amount", "buyPriceUsd"})
public class PortfolioEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coin_id", nullable = false)
    private Coin coin;

    @Column(nullable = false, precision = 24, scale = 8)
    private BigDecimal amount;

    @Column(name = "buy_price_usd", nullable = false, precision = 24, scale = 8)
    private BigDecimal buyPriceUsd;

    @Column(name = "bought_at", nullable = false)
    private Instant boughtAt;

    @Column(length = 256)
    private String comment;

    @PrePersist
    void onCreate() {
        if (boughtAt == null) {
            boughtAt = Instant.now();
        }
    }
}
