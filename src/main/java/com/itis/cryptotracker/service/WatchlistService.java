package com.itis.cryptotracker.service;

import com.itis.cryptotracker.mapper.WatchlistMapper;
import com.itis.cryptotracker.model.Coin;
import com.itis.cryptotracker.model.User;
import com.itis.cryptotracker.model.WatchlistItem;
import com.itis.cryptotracker.dto.response.WatchlistItemDto;
import com.itis.cryptotracker.exception.ConflictException;
import com.itis.cryptotracker.repository.WatchlistItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WatchlistService {

    private final WatchlistItemRepository watchlistRepository;
    private final UserService userService;
    private final CoinService coinService;
    private final WatchlistMapper watchlistMapper;

    @Transactional(readOnly = true)
    public List<WatchlistItemDto> findForUser(Long userId) {
        return watchlistRepository.findAllByUserIdOrderByAddedAtDesc(userId).stream()
                .map(watchlistMapper::toDto)
                .toList();
    }

    @Transactional
    public void add(Long userId, Long coinId) {
        if (watchlistRepository.existsByUserIdAndCoinId(userId, coinId)) {
            throw new ConflictException("Монета уже в вашем watchlist");
        }
        User user = userService.findById(userId);
        Coin coin = coinService.findById(coinId);
        watchlistRepository.save(WatchlistItem.builder().user(user).coin(coin).build());
        log.info("User {} added coin {} to watchlist", userId, coinId);
    }

    @Transactional
    public void remove(Long userId, Long coinId) {
        watchlistRepository.deleteByUserIdAndCoinId(userId, coinId);
        log.info("User {} removed coin {} from watchlist", userId, coinId);
    }

    private WatchlistItemDto toDto(WatchlistItem item) {
        Coin coin = item.getCoin();
        return new WatchlistItemDto(
                item.getId(),
                coin.getId(),
                coin.getCoinGeckoId(),
                coin.getSymbol(),
                coin.getName(),
                coin.getImageUrl(),
                coin.getCurrentPriceUsd(),
                item.getAddedAt()
        );
    }
}
