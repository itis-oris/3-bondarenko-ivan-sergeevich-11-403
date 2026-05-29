package com.itis.cryptotracker.dto.form;

import com.itis.cryptotracker.model.enums.AlertDirection;
import com.itis.cryptotracker.model.enums.NotificationChannel;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class AlertForm {

    private Long id;

    @NotNull(message = "Выберите монету")
    private Long coinId;

    @NotNull(message = "Выберите направление")
    private AlertDirection direction;

    @NotNull(message = "Укажите целевую цену")
    @DecimalMin(value = "0.00000001", message = "Целевая цена должна быть положительной")
    private BigDecimal targetPrice;

    @NotNull(message = "Выберите канал уведомления")
    private NotificationChannel channel;

    @Size(max = 256)
    private String comment;
}
