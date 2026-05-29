package com.itis.cryptotracker.mapper;

import com.itis.cryptotracker.dto.response.NotificationDto;
import com.itis.cryptotracker.model.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(source = "alert.id", target = "alertId")
    @Mapping(source = "alert.coin.symbol", target = "coinSymbol")
    NotificationDto toDto(Notification notification);
}