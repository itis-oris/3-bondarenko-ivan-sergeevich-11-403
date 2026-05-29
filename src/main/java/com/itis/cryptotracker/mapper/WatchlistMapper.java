package com.itis.cryptotracker.mapper;

import com.itis.cryptotracker.dto.response.WatchlistItemDto;
import com.itis.cryptotracker.model.WatchlistItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface WatchlistMapper {
    @Mapping(source = "coin.id", target = "coinId")
    @Mapping(source = "coin.coinGeckoId", target = "coinGeckoId")
    @Mapping(source = "coin.symbol", target = "coinSymbol")
    @Mapping(source = "coin.name", target = "coinName")
    @Mapping(source = "coin.imageUrl", target = "imageUrl")
    @Mapping(source = "coin.currentPriceUsd", target = "currentPriceUsd")
    WatchlistItemDto toDto(WatchlistItem item);
}