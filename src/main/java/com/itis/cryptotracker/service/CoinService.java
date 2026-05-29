package com.itis.cryptotracker.service;

import com.itis.cryptotracker.mapper.CoinDtoMapper;
import com.itis.cryptotracker.model.Coin;
import com.itis.cryptotracker.dto.response.CoinDto;
import com.itis.cryptotracker.exception.NotFoundException;
import com.itis.cryptotracker.repository.CoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoinService {

    private final CoinRepository coinRepository;
    private final CoinDtoMapper coinMapper;

    @Transactional(readOnly = true)
    public List<CoinDto> findAll() {
        return coinRepository.findAllWithTags().stream()
                .map(coinMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CoinDto> search(String query, List<String> tags) {
        return coinRepository.searchByQueryAndTags(query, tags).stream()
                .map(coinMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Coin findById(Long id) {
        return coinRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Монета не найдена: %d".formatted(id)));
    }


}
