package com.itis.cryptotracker.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CoinPriceDto implements Serializable {
    private String coinGeckoId;
    private BigDecimal priceUsd;
}
