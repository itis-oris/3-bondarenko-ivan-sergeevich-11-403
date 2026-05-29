package com.itis.cryptotracker.service;

import com.itis.cryptotracker.client.CoinGeckoClient;
import com.itis.cryptotracker.config.CoinDetails;
import com.itis.cryptotracker.model.Coin;
import com.itis.cryptotracker.model.Tag;
import com.itis.cryptotracker.exception.ConflictException;
import com.itis.cryptotracker.repository.AlertRepository;
import com.itis.cryptotracker.repository.CoinRepository;
import com.itis.cryptotracker.repository.NotificationRepository;
import com.itis.cryptotracker.repository.PortfolioEntryRepository;
import com.itis.cryptotracker.repository.PriceSnapshotRepository;
import com.itis.cryptotracker.repository.TagRepository;
import com.itis.cryptotracker.repository.WatchlistItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCoinService {

    private final CoinGeckoClient coinGeckoClient;
    private final CoinService coinService;
    private final CoinRepository coinRepository;
    private final TagRepository tagRepository;
    private final NotificationRepository notificationRepository;
    private final AlertRepository alertRepository;
    private final WatchlistItemRepository watchlistItemRepository;
    private final PortfolioEntryRepository portfolioEntryRepository;
    private final PriceSnapshotRepository priceSnapshotRepository;

    @Transactional(readOnly = true)
    public List<Tag> findAllTags() {
        return tagRepository.findAll();
    }

    @Transactional
    public void addCoin(String coinGeckoId, List<Long> tagIds) {
        if (coinRepository.findByCoinGeckoId(coinGeckoId).isPresent()) {
            throw new ConflictException("Монета '%s' уже есть в каталоге".formatted(coinGeckoId));
        }
        CoinDetails details = coinGeckoClient.fetchDetails(coinGeckoId);
        Set<Tag> tags = new HashSet<>();
        if (tagIds != null) {
            tags.addAll(tagRepository.findAllById(tagIds));
        }
        Coin coin = Coin.builder()
                .coinGeckoId(details.getCoinGeckoId())
                .symbol(details.getSymbol() == null ? coinGeckoId : details.getSymbol().toUpperCase())
                .name(details.getName() == null ? coinGeckoId : details.getName())
                .imageUrl(details.getImageUrl())
                .tags(tags)
                .build();
        coinRepository.save(coin);
        log.info("Added coin coinGeckoId={}", coinGeckoId);
    }

    @Transactional
    public void deleteCoin(Long coinId) {
        Coin coin = coinService.findById(coinId);
        notificationRepository.deleteAllByAlertCoinId(coinId);
        alertRepository.deleteAllByCoinId(coinId);
        watchlistItemRepository.deleteAllByCoinId(coinId);
        portfolioEntryRepository.deleteAllByCoinId(coinId);
        priceSnapshotRepository.deleteAllByCoinId(coinId);
        coinRepository.delete(coin);
        log.info("Deleted coin id={}", coinId);
    }

    @Transactional
    public void addTag(String name) {
        if (tagRepository.findByName(name).isPresent()) {
            throw new ConflictException("Тег '%s' уже существует".formatted(name));
        }
        tagRepository.save(Tag.builder()
                .name(name)
                .build());
        log.info("Added tag name={}", name);
    }
}
