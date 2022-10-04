package de.c24.finacc.klt.currency_converter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.LinkedHashMap;

@Data
public class LatestResponseDto {
    @JsonProperty("base")
    private String from;

    private String date;

    @JsonProperty("rates")
    private LinkedHashMap<String, String> to;

    @JsonProperty("success")
    private String status;

    private String timestamp;
}
