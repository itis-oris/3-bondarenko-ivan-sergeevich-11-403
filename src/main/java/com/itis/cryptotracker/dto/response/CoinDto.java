package com.itis.cryptotracker.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Schema(description = "Криптовалюта")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoinDto implements Serializable {
    private Long id;
    private String coinGeckoId;
    private String symbol;
    private String name;
    private String imageUrl;
    private BigDecimal currentPriceUsd;
    private Instant priceUpdatedAt;
    private Set<String> tags;
}
