package com.itis.cryptotracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "coins")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "coinGeckoId", "symbol", "name"})
public class Coin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coingecko_id", nullable = false, unique = true, length = 64)
    private String coinGeckoId;

    @Column(nullable = false, length = 16)
    private String symbol;

    @Column(nullable = false, length = 128)
    private String name;

    @Column(name = "image_url", length = 512)
    private String imageUrl;

    @Column(name = "current_price_usd", precision = 24, scale = 8)
    private BigDecimal currentPriceUsd;

    @Column(name = "price_updated_at")
    private Instant priceUpdatedAt;

    @ManyToMany
    @JoinTable(
            name = "coin_tags",
            joinColumns = @JoinColumn(name = "coin_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();
}
