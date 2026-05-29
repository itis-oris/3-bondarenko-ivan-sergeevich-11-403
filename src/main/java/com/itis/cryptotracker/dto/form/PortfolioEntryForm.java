package com.itis.cryptotracker.dto.form;

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
public class PortfolioEntryForm {

    private Long id;

    @NotNull(message = "Выберите монету")
    private Long coinId;

    @NotNull(message = "Укажите количество")
    @DecimalMin(value = "0.00000001", message = "Количество должно быть положительным")
    private BigDecimal amount;

    @NotNull(message = "Укажите цену покупки")
    @DecimalMin(value = "0.00000001", message = "Цена должна быть положительной")
    private BigDecimal buyPriceUsd;

    @Size(max = 256)
    private String comment;
}
