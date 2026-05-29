package com.itis.cryptotracker.dto.response;

import com.itis.cryptotracker.model.enums.AlertDirection;
import com.itis.cryptotracker.model.enums.AlertStatus;
import com.itis.cryptotracker.model.enums.NotificationChannel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Schema(description = "Алерт пользователя")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertResponse implements Serializable {
    private Long id;
    private Long coinId;
    private String coinSymbol;
    private String coinName;
    private AlertDirection direction;
    private BigDecimal targetPrice;
    private NotificationChannel channel;
    private AlertStatus status;
    private String comment;
    private Instant createdAt;
    private Instant triggeredAt;
}
