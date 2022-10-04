package de.c24.finacc.klt.currency_converter.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatesDto {
    private String baseCurrency;
    private String toCurrency;
    private BigDecimal baseConversion;
    private BigDecimal convertedValue;
}
