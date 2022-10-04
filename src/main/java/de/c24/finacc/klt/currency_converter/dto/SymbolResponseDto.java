package de.c24.finacc.klt.currency_converter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SymbolResponseDto {

    @JsonProperty("success")
    private String status;

    @JsonProperty("symbols")
    private LinkedHashMap<String, String> symbols;
}
