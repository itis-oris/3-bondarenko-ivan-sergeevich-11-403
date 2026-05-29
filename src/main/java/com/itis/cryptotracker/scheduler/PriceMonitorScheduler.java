package com.itis.cryptotracker.scheduler;

import com.itis.cryptotracker.model.Coin;
import com.itis.cryptotracker.service.AlertMatcher;
import com.itis.cryptotracker.service.PriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PriceMonitorScheduler {

    private final PriceService priceService;
    private final AlertMatcher alertMatcher;

    @Scheduled(fixedDelayString = "${app.coingecko.poll-interval-ms}", initialDelay = 5000)
    public void tick() {
        try {
            List<Coin> updated = priceService.refreshAllPrices();
            if (!updated.isEmpty()) {
                alertMatcher.matchAndFire(updated);
            }
        } catch (Exception ex) {
            log.error("Price monitor tick failed", ex);
        }
    }
}
