package com.itis.cryptotracker.mapper;

import com.itis.cryptotracker.dto.response.PnlResult;
import com.itis.cryptotracker.dto.response.PortfolioEntryDto;
import com.itis.cryptotracker.model.PortfolioEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PortfolioMapper {

    @Mapping(source = "entry.coin.id", target = "coinId")
    @Mapping(source = "entry.coin.symbol", target = "coinSymbol")
    @Mapping(source = "entry.coin.name", target = "coinName")
    @Mapping(source = "entry.coin.currentPriceUsd", target = "currentPriceUsd")
    @Mapping(source = "pnl.currentValue", target = "currentValueUsd")
    @Mapping(source = "pnl.pnl", target = "pnlUsd")
    @Mapping(source = "pnl.pnlPercent", target = "pnlPercent")
    PortfolioEntryDto toDto(PortfolioEntry entry, PnlResult pnl);
}