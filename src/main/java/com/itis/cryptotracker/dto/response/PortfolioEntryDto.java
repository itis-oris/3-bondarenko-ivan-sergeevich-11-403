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
public class PortfolioEntryDto implements Serializable {
    private Long id;
    private Long coinId;
    private String coinSymbol;
    private String coinName;
    private BigDecimal amount;
    private BigDecimal buyPriceUsd;
    private BigDecimal currentPriceUsd;
    private BigDecimal currentValueUsd;
    private BigDecimal pnlUsd;
    private BigDecimal pnlPercent;
    private Instant boughtAt;
    private String comment;
}
