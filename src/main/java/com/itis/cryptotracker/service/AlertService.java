package com.itis.cryptotracker.service;

import com.itis.cryptotracker.model.Alert;
import com.itis.cryptotracker.model.enums.AlertDirection;
import com.itis.cryptotracker.model.enums.AlertStatus;
import com.itis.cryptotracker.model.Coin;
import com.itis.cryptotracker.model.User;
import com.itis.cryptotracker.dto.form.AlertForm;
import com.itis.cryptotracker.dto.request.AlertRequest;
import com.itis.cryptotracker.exception.NotFoundException;
import com.itis.cryptotracker.repository.AlertRepository;
import com.itis.cryptotracker.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final UserService userService;
    private final CoinService coinService;
    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public List<Alert> findUserAlerts(Long userId) {
        return alertRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional(readOnly = true)
    public List<Alert> searchUserAlerts(Long userId, AlertStatus status, AlertDirection direction, String query) {
        return alertRepository.searchUserAlerts(userId, status, direction, query);
    }

    @Transactional(readOnly = true)
    public Alert findOwned(Long alertId, Long userId) {
        return alertRepository.findByIdAndUserId(alertId, userId)
                .orElseThrow(() -> new NotFoundException("Alert not found: %d".formatted(alertId)));
    }

    @Transactional(readOnly = true)
    public List<Alert> findFrequentlyTriggered(Long userId, Instant since, long minTriggers) {
        return alertRepository.findFrequentlyTriggered(userId, since, minTriggers);
    }

    @Transactional
    public Alert createFromForm(Long userId, AlertForm form) {
        User user = userService.findById(userId);
        Coin coin = coinService.findById(form.getCoinId());

        Alert alert = Alert.builder()
                .user(user)
                .coin(coin)
                .direction(form.getDirection())
                .targetPrice(form.getTargetPrice())
                .channel(form.getChannel())
                .status(AlertStatus.ACTIVE)
                .comment(form.getComment())
                .build();

        Alert saved = alertRepository.save(alert);
        log.info("Created alert id={} userId={} coinId={}", saved.getId(), userId, coin.getId());
        return saved;
    }

    @Transactional
    public Alert createFromRequest(Long userId, AlertRequest req) {
        User user = userService.findById(userId);
        Coin coin = coinService.findById(req.getCoinId());
        Alert alert = Alert.builder()
                .user(user)
                .coin(coin)
                .direction(req.getDirection())
                .targetPrice(req.getTargetPrice())
                .channel(req.getChannel())
                .status(AlertStatus.ACTIVE)
                .comment(req.getComment())
                .build();
        Alert saved = alertRepository.save(alert);
        log.info("Created alert id={} userId={} coinId={}", saved.getId(), userId, coin.getId());
        return saved;
    }

    @Transactional
    public Alert update(Long alertId, Long userId, AlertForm form) {
        Alert alert = findOwned(alertId, userId);

        if (!form.getCoinId().equals(alert.getCoin().getId())) {
            alert.setCoin(coinService.findById(form.getCoinId()));
        }
        alert.setDirection(form.getDirection());
        alert.setTargetPrice(form.getTargetPrice());
        alert.setChannel(form.getChannel());
        alert.setComment(form.getComment());
        return alert;
    }

    @Transactional
    public void delete(Long alertId, Long userId) {
        Alert alert = findOwned(alertId, userId);
        notificationRepository.deleteAllByAlert_Id(alertId);
        alertRepository.delete(alert);
        log.info("Deleted alert id={} userId={}", alertId, userId);
    }

    @Transactional
    public Alert reactivate(Long alertId, Long userId) {
        Alert alert = findOwned(alertId, userId);
        alert.setStatus(AlertStatus.ACTIVE);
        alert.setTriggeredAt(null);
        return alert;
    }

    @Transactional
    public Alert disable(Long alertId, Long userId) {
        Alert alert = findOwned(alertId, userId);
        alert.setStatus(AlertStatus.DISABLED);
        return alert;
    }
}
