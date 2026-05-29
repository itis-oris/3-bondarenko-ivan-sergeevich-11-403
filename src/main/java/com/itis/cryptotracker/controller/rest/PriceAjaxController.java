package com.itis.cryptotracker.controller.rest;

import com.itis.cryptotracker.dto.response.CoinDto;
import com.itis.cryptotracker.dto.response.NotificationDto;
import com.itis.cryptotracker.security.UserPrincipal;
import com.itis.cryptotracker.service.CoinService;
import com.itis.cryptotracker.service.NotificationService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Hidden
@RestController
@RequestMapping("/api/ui")
@RequiredArgsConstructor
public class PriceAjaxController {

    private final CoinService coinService;
    private final NotificationService notificationService;

    @GetMapping("/prices")
    public List<CoinDto> prices() {
        return coinService.findAll();
    }

    @GetMapping("/notifications")
    public List<NotificationDto> notifications(@AuthenticationPrincipal UserPrincipal principal) {
        return notificationService.findRecentForUser(principal.getId(), 10);
    }

    @GetMapping("/notifications/unread-count")
    public Map<String, Long> unread(@AuthenticationPrincipal UserPrincipal principal) {
        return Map.of("unread", notificationService.countUnread(principal.getId()));
    }

    @PostMapping("/notifications/mark-read")
    public Map<String, Integer> markAllRead(@AuthenticationPrincipal UserPrincipal principal) {
        return Map.of("updated", notificationService.markAllRead(principal.getId()));
    }
}
