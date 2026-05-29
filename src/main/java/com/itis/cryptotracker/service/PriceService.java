package com.itis.cryptotracker.service;

import com.itis.cryptotracker.client.CoinGeckoClient;
import com.itis.cryptotracker.client.CoinPriceDto;
import com.itis.cryptotracker.exception.ServiceException;
import com.itis.cryptotracker.model.Coin;
import com.itis.cryptotracker.model.PriceSnapshot;
import com.itis.cryptotracker.repository.CoinRepository;
import com.itis.cryptotracker.repository.PriceSnapshotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PriceService {

    private final CoinRepository coinRepository;
    private final PriceSnapshotRepository snapshotRepository;
    private final CoinGeckoClient client;


    @Transactional
    public List<Coin> refreshAllPrices() {
        List<Coin> coins = coinRepository.findAll();
        if (coins.isEmpty()) {
            return List.of();
        }
        List<String> ids = coins.stream()
                .map(Coin::getCoinGeckoId)
                .toList();

        List<CoinPriceDto> prices;
        try {
            prices = client.fetchPrices(ids);
        } catch (ServiceException ex) {
            log.warn("Skipping price refresh CoinGecko unavailable: {}", ex.getMessage());
            return List.of();
        }

        Map<String, Coin> byId = new HashMap<>();
        coins.forEach(c -> byId.put(c.getCoinGeckoId(), c));

        Instant now = Instant.now();
        for (CoinPriceDto p : prices) {
            Coin coin = byId.get(p.getCoinGeckoId());
            if (coin == null) continue;
            coin.setCurrentPriceUsd(p.getPriceUsd());
            coin.setPriceUpdatedAt(now);
            snapshotRepository.save(PriceSnapshot.builder()
                    .coin(coin)
                    .priceUsd(p.getPriceUsd())
                    .recordedAt(now)
                    .build());
        }
        log.debug("Refreshed prices for {} coins", prices.size());
        return prices.stream()
                .map(p -> byId.get(p.getCoinGeckoId()))
                .filter(Objects::nonNull)
                .toList();
    }
}
