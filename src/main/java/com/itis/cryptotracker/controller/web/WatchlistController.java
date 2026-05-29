package com.itis.cryptotracker.controller.web;

import com.itis.cryptotracker.security.UserPrincipal;
import com.itis.cryptotracker.service.WatchlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/watchlist")
@RequiredArgsConstructor
public class WatchlistController {

    private final WatchlistService watchlistService;

    @PostMapping("/{coinId}")
    public String add(@AuthenticationPrincipal UserPrincipal principal,
                      @PathVariable Long coinId,
                      @RequestParam(required = false) String redirect) {
        watchlistService.add(principal.getId(), coinId);
        return "redirect:" + (redirect != null ? redirect : "/dashboard");
    }

    @PostMapping("/{coinId}/delete")
    public String remove(@AuthenticationPrincipal UserPrincipal principal,
                         @PathVariable Long coinId,
                         @RequestParam(required = false) String redirect) {
        watchlistService.remove(principal.getId(), coinId);
        return "redirect:" + (redirect != null ? redirect : "/dashboard");
    }
}
