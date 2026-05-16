package com.financli.service;

import com.financli.model.CotacaoResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class CotacaoServiceIntegrationTest {

    @Autowired
    private CotacaoService cotacaoService;

    @Test
    void deveBuscarCotacoesComSucesso() {
        CotacaoResponse resposta = cotacaoService.buscarCotacoes();

        assertThat(resposta).isNotNull();
        assertThat(resposta.usdBrl()).isNotNull();
        assertThat(resposta.eurBrl()).isNotNull();
        assertThat(Double.parseDouble(resposta.usdBrl().bid())).isGreaterThan(0.0);
        assertThat(Double.parseDouble(resposta.eurBrl().bid())).isGreaterThan(0.0);
    }
}