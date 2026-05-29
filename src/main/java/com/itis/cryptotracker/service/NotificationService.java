package com.itis.cryptotracker.service;

import com.itis.cryptotracker.mapper.NotificationMapper;
import com.itis.cryptotracker.model.Alert;
import com.itis.cryptotracker.model.enums.AlertDirection;
import com.itis.cryptotracker.model.enums.AlertStatus;
import com.itis.cryptotracker.model.Notification;
import com.itis.cryptotracker.model.enums.NotificationChannel;
import com.itis.cryptotracker.dto.response.NotificationDto;
import com.itis.cryptotracker.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final EmailService emailService;
    private final NotificationMapper notificationMapper;

    @Transactional(readOnly = true)
    public List<NotificationDto> findRecentForUser(Long userId, int limit) {
        return notificationRepository.findAllByUserIdOrderByCreatedAtDesc(
                        userId, PageRequest.of(0, limit))
                .stream()
                .map(notificationMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public long countUnread(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Transactional
    public int markAllRead(Long userId) {
        return notificationRepository.markAllReadForUser(userId);
    }

    @Transactional
    public Notification fireAlert(Alert alert, BigDecimal triggeredPrice) {
        String message = buildMessage(alert, triggeredPrice);
        Notification notification = notificationRepository.save(Notification.builder()
                .user(alert.getUser())
                .alert(alert)
                .triggeredPrice(triggeredPrice)
                .channel(alert.getChannel())
                .isRead(false)
                .message(message)
                .build());

        alert.setStatus(AlertStatus.TRIGGERED);
        alert.setTriggeredAt(notification.getCreatedAt());

        NotificationDto dto = notificationMapper.toDto(notification);

        if (alert.getChannel() == NotificationChannel.WEBSOCKET ||
                alert.getChannel() == NotificationChannel.BOTH) {
            messagingTemplate.convertAndSendToUser(
                    alert.getUser().getUsername(),
                    "/queue/notifications",
                    dto
            );
        }
        if ((alert.getChannel() == NotificationChannel.EMAIL ||
                alert.getChannel() == NotificationChannel.BOTH)
                && alert.getUser().isEmailNotificationsEnabled()) {
            emailService.send(
                    alert.getUser().getEmail(),
                    buildEmailSubject(alert, triggeredPrice),
                    message
            );
        }

        log.info("Fired alert id={} userId={} price={}",
                alert.getId(), alert.getUser().getId(), triggeredPrice);
        return notification;
    }

    private String buildMessage(Alert alert, BigDecimal price) {
        String direction = alert.getDirection() == AlertDirection.ABOVE ? "выше" : "ниже";
        return "%s сейчас %s - сработало оповещение %s для %s (цель: %s)".formatted(
                alert.getCoin().getSymbol(),
                price.stripTrailingZeros().toPlainString(),
                direction,
                alert.getCoin().getName(),
                alert.getTargetPrice().stripTrailingZeros().toPlainString()
        );
    }

    private String buildEmailSubject(Alert alert, BigDecimal price) {
        String direction = alert.getDirection() == AlertDirection.ABOVE ? "выше" : "ниже";
        return "[CryptoTracker] %s %s %s".formatted(
                alert.getCoin().getSymbol(),
                direction,
                price.stripTrailingZeros().toPlainString()
        );
    }
}