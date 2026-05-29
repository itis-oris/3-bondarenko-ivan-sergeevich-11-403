package com.itis.cryptotracker.dto.request;

import com.itis.cryptotracker.model.enums.AlertDirection;
import com.itis.cryptotracker.model.enums.NotificationChannel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Schema(description = "Тело запроса для создания/обновления алерта через REST API")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertRequest {

    @Schema(description = "ID монеты", example = "1")
    @NotNull
    private Long coinId;

    @Schema(description = "Направление пересечения цены", example = "BELOW")
    @NotNull
    private AlertDirection direction;

    @Schema(description = "Целевая цена в USD", example = "60000.00")
    @NotNull
    @DecimalMin("0.00000001")
    private BigDecimal targetPrice;

    @Schema(description = "Канал уведомления", example = "BOTH")
    @NotNull
    private NotificationChannel channel;

    @Schema(description = "Комментарий", example = "Что-угодно")
    @Size(max = 256)
    private String comment;
}
