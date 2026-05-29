package com.itis.cryptotracker.dto.response;

import com.itis.cryptotracker.model.enums.NotificationChannel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto implements Serializable {
    private Long id;
    private Long alertId;
    private String coinSymbol;
    private String message;
    private BigDecimal triggeredPrice;
    private NotificationChannel channel;
    private boolean read;
    private Instant createdAt;
}
