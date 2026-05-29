package com.itis.cryptotracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchlistItemDto implements Serializable {
    private Long id;
    private Long coinId;
    private String coinGeckoId;
    private String coinSymbol;
    private String coinName;
    private String imageUrl;
    private BigDecimal currentPriceUsd;
    private Instant addedAt;
}