package com.itis.cryptotracker.converter;

import com.itis.cryptotracker.model.Coin;
import com.itis.cryptotracker.exception.NotFoundException;
import com.itis.cryptotracker.repository.CoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class StringToCoinConverter implements Converter<String, Coin> {

    private final CoinRepository coinRepository;

    @Override
    public Coin convert(@NonNull String source) {
        return coinRepository.findByCoinGeckoId(source)
                .orElseThrow(() -> new NotFoundException("Coin not found: %s".formatted(source)));
    }
}
