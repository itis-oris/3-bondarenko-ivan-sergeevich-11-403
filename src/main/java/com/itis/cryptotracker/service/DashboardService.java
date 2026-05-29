package com.itis.cryptotracker.service;

import com.itis.cryptotracker.mapper.AlertMapper;
import com.itis.cryptotracker.dto.response.DashboardData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private static final int NOTIFICATION_LIMIT = 10;
    private static final long FREQUENT_WINDOW_DAYS = 7;
    private static final long FREQUENT_MIN_TRIGGERS = 3;

    private final AlertService alertService;
    private final AlertMapper alertMapper;
    private final WatchlistService watchlistService;
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public DashboardData getDashboardData(Long userId) {
        Instant since = Instant.now().minus(FREQUENT_WINDOW_DAYS, ChronoUnit.DAYS);
        return new DashboardData(
                alertService.findUserAlerts(userId).stream().map(alertMapper::toResponse).toList(),
                watchlistService.findForUser(userId),
                notificationService.findRecentForUser(userId, NOTIFICATION_LIMIT),
                notificationService.countUnread(userId),
                alertService.findFrequentlyTriggered(userId, since, FREQUENT_MIN_TRIGGERS)
                        .stream()
                        .map(alertMapper::toResponse)
                        .toList()
        );
    }
}
