package com.itis.cryptotracker.mapper;

import com.itis.cryptotracker.model.Alert;
import com.itis.cryptotracker.dto.response.AlertResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlertMapper {
    @Mapping(source = "coin.id", target = "coinId")
    @Mapping(source = "coin.symbol", target = "coinSymbol")
    @Mapping(source = "coin.name", target = "coinName")
    AlertResponse toResponse(Alert alert);
}