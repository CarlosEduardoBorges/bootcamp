package com.financli.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CotacaoResponse(
    @JsonProperty("USDBRL") Cotacao usdBrl,
    @JsonProperty("EURBRL") Cotacao eurBrl
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Cotacao(String bid, String ask, String name) {
    }
}
