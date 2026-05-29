package com.itis.cryptotracker.controller.web;

import com.itis.cryptotracker.dto.response.DashboardData;
import com.itis.cryptotracker.security.UserPrincipal;
import com.itis.cryptotracker.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        DashboardData data = dashboardService.getDashboardData(principal.getId());
        model.addAttribute("alerts", data.getAlerts());
        model.addAttribute("watchlist", data.getWatchlist());
        model.addAttribute("notifications", data.getNotifications());
        model.addAttribute("unread", data.getUnread());
        model.addAttribute("frequentAlerts", data.getFrequentAlerts());
        return "dashboard";
    }
}
