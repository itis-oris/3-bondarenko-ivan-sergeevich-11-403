package com.itis.cryptotracker.controller.web;

import com.itis.cryptotracker.mapper.CoinDtoMapper;
import com.itis.cryptotracker.model.Coin;
import com.itis.cryptotracker.dto.response.CoinDto;
import com.itis.cryptotracker.dto.response.WatchlistItemDto;
import com.itis.cryptotracker.security.UserPrincipal;
import com.itis.cryptotracker.service.CoinService;
import com.itis.cryptotracker.service.SnapshotService;
import com.itis.cryptotracker.service.WatchlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class CoinController {

    private final CoinService coinService;
    private final CoinDtoMapper coinMapper;
    private final WatchlistService watchlistService;
    private final SnapshotService snapshotService;

    @GetMapping("/coins")
    public String list(@RequestParam(required = false) String q,
                       @RequestParam(required = false) List<String> tags,
                       @AuthenticationPrincipal UserPrincipal principal,
                       Model model) {
        List<CoinDto> coins = (q == null && (tags == null || tags.isEmpty()))
                ? coinService.findAll()
                : coinService.search(q, tags);
        model.addAttribute("coins", coins);
        model.addAttribute("query", q);
        model.addAttribute("activeTags", tags == null ? List.of() : tags);
        if (principal != null) {
            Set<Long> watchlistCoinIds = watchlistService.findForUser(principal.getId()).stream()
                    .map(WatchlistItemDto::getCoinId)
                    .collect(Collectors.toSet());
            model.addAttribute("watchlistCoinIds", watchlistCoinIds);
        }
        return "coins/list";
    }


    @GetMapping("/coins/{coinGeckoId}")
    public String detail(@PathVariable("coinGeckoId") Coin coin,
                         @AuthenticationPrincipal UserPrincipal principal,
                         Model model) {
        model.addAttribute("coin", coinMapper.toDto(coin));
        model.addAttribute("snapshots",
                snapshotService.getAllByCoinId(coin.getId(), PageRequest.of(0, 50)));
        boolean inWatchlist = principal != null
                && watchlistService.findForUser(principal.getId()).stream()
                        .anyMatch(item -> item.getCoinId().equals(coin.getId()));
        model.addAttribute("inWatchlist", inWatchlist);
        return "coins/detail";
    }
}
