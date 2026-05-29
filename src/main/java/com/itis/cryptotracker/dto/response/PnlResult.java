package com.itis.cryptotracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class PnlResult {
    private final BigDecimal currentValue;
    private final BigDecimal pnl;
    private final BigDecimal pnlPercent;
}