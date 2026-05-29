package com.itis.cryptotracker.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoinDetails implements Serializable {
    private String coinGeckoId;
    private String symbol;
    private String name;
    private String imageUrl;
}