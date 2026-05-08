package com.financli.service;

import com.financli.model.CotacaoResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class CotacaoService {

    private static final String COTACAO_URL = "https://economia.awesomeapi.com.br/json/last/USD-BRL,EUR-BRL";

    private final RestClient restClient;

    public CotacaoService() {
        this.restClient = RestClient.create();
    }

    public CotacaoResponse buscarCotacoes() {
        try {
            return restClient.get()
                    .uri(COTACAO_URL)
                    .retrieve()
                    .body(CotacaoResponse.class);
        } catch (RestClientException e) {
            return null;
        }
    }
}
