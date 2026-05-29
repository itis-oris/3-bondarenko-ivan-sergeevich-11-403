package com.itis.cryptotracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DashboardData {

        private List<AlertResponse> alerts;
        private List<WatchlistItemDto> watchlist;
        private List<NotificationDto> notifications;
        private Long unread;
        private List<AlertResponse> frequentAlerts;

}
