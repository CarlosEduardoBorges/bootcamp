package com.financli.service;

import com.financli.model.CotacaoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class CotacaoService {

    private final RestClient restClient;
    private final String cotacaoUrl;

    public CotacaoService(@Value("${cotacao.api.url}") String cotacaoUrl) {
        this.restClient = RestClient.create();
        this.cotacaoUrl = cotacaoUrl;
    }

    public CotacaoResponse buscarCotacoes() {
        try {
            return restClient.get()
                    .uri(cotacaoUrl)
                    .retrieve()
                    .body(CotacaoResponse.class);
        } catch (RestClientException e) {
            return null;
        }
    }
}
