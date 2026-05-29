package com.itis.cryptotracker.service;

import com.itis.cryptotracker.model.Alert;
import com.itis.cryptotracker.model.enums.AlertDirection;
import com.itis.cryptotracker.model.Coin;
import com.itis.cryptotracker.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertMatcher {

    private final AlertRepository alertRepository;
    private final NotificationService notificationService;

    @Transactional
    public int matchAndFire(List<Coin> coins) {

        if (coins == null || coins.isEmpty()) {
            return 0;
        }

        Map<Long, Coin> byId = coins.stream()
                .collect(Collectors.toMap(Coin::getId, Function.identity()));

        List<Alert> alerts = alertRepository.findActiveAlertsByCoinIds(
                byId.keySet()
                        .stream()
                        .toList()
        );

        int fired = 0;
        for (Alert alert : alerts) {
            Coin coin = byId.get(alert.getCoin().getId());
            if (coin == null || coin.getCurrentPriceUsd() == null) continue;
            if (crossed(alert.getDirection(), coin.getCurrentPriceUsd(), alert.getTargetPrice())) {
                notificationService.fireAlert(alert, coin.getCurrentPriceUsd());
                fired++;
            }
        }
        if (fired > 0) {
            log.info("Fired {} alerts in this match cycle", fired);
        }
        return fired;
    }

    private boolean crossed(AlertDirection direction, BigDecimal current, BigDecimal target) {
        return switch (direction) {
            case ABOVE -> current.compareTo(target) >= 0;
            case BELOW -> current.compareTo(target) <= 0;
        };
    }
}
