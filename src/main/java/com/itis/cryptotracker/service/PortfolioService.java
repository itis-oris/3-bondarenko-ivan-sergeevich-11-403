package com.itis.cryptotracker.service;

import com.itis.cryptotracker.mapper.PortfolioMapper;
import com.itis.cryptotracker.dto.response.PnlResult;
import com.itis.cryptotracker.model.Coin;
import com.itis.cryptotracker.model.PortfolioEntry;
import com.itis.cryptotracker.model.User;
import com.itis.cryptotracker.dto.form.PortfolioEntryForm;
import com.itis.cryptotracker.dto.response.PortfolioEntryDto;
import com.itis.cryptotracker.exception.NotFoundException;
import com.itis.cryptotracker.repository.PortfolioEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioEntryRepository portfolioRepository;
    private final UserService userService;
    private final CoinService coinService;
    private final PortfolioMapper portfolioMapper;

    @Transactional(readOnly = true)
    public List<PortfolioEntryDto> findForUser(Long userId) {
        return portfolioRepository.findAllByUserIdOrderByBoughtAtDesc(userId).stream()
                .map(p -> portfolioMapper.toDto(p, calculatePnl(p)))
                .toList();
    }

    @Transactional
    public PortfolioEntry add(Long userId, PortfolioEntryForm form) {
        User user = userService.findById(userId);
        Coin coin = coinService.findById(form.getCoinId());
        PortfolioEntry entry = PortfolioEntry.builder()
                .user(user)
                .coin(coin)
                .amount(form.getAmount())
                .buyPriceUsd(form.getBuyPriceUsd())
                .comment(form.getComment())
                .build();
        return portfolioRepository.save(entry);
    }

    @Transactional
    public void delete(Long entryId, Long userId) {
        PortfolioEntry entry = portfolioRepository.findByIdAndUserId(entryId, userId)
                .orElseThrow(() -> new NotFoundException("Portfolio entry not found: %d".formatted(entryId)));
        portfolioRepository.delete(entry);
        log.info("Deleted portfolio entry id={} userId={}", entryId, userId);
    }

    private PnlResult calculatePnl(PortfolioEntry entry) {
        BigDecimal current = entry.getCoin().getCurrentPriceUsd();
        if (current == null) {
            return new PnlResult(null, null, null);
        }
        BigDecimal currentValue = current.multiply(entry.getAmount());
        BigDecimal cost = entry.getBuyPriceUsd().multiply(entry.getAmount());
        BigDecimal pnl = currentValue.subtract(cost);
        BigDecimal pnlPercent = cost.signum() == 0
                ? null
                : pnl.multiply(BigDecimal.valueOf(100)).divide(cost, 4, RoundingMode.HALF_UP);
        return new PnlResult(currentValue, pnl, pnlPercent);
    }

}
