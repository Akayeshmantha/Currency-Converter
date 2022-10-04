package de.c24.finacc.klt.currency_converter.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SymbolDto {

    private String key;

    private String value;

    private String keyValue;

}
