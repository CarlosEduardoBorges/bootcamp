package com.financli.service;

import com.financli.model.CotacaoResponse;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {"cotacao.api.url=http://localhost:9876/json/last/USD-BRL,EUR-BRL"})
@ActiveProfiles("test")
class CotacaoServiceIntegrationTest {

    static WireMockServer wireMock = new WireMockServer(
            WireMockConfiguration.wireMockConfig().port(9876));

    @BeforeAll
    static void iniciarWireMock() {
        wireMock.start();
    }

    @AfterAll
    static void encerrarWireMock() {
        wireMock.stop();
    }

    @Autowired
    private CotacaoService cotacaoService;

    @Test
    void deveBuscarCotacoesComSucesso() {
        wireMock.stubFor(WireMock.get(WireMock.urlPathEqualTo("/json/last/USD-BRL,EUR-BRL"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "USDBRL": {"bid": "5.75", "ask": "5.76",
                                               "name": "Dólar Americano/Real Brasileiro"},
                                    "EURBRL": {"bid": "6.20", "ask": "6.21",
                                               "name": "Euro/Real Brasileiro"}
                                }
                                """)
                )
        );

        CotacaoResponse resposta = cotacaoService.buscarCotacoes();

        assertThat(resposta).isNotNull();
        assertThat(resposta.usdBrl()).isNotNull();
        assertThat(resposta.eurBrl()).isNotNull();
        assertThat(Double.parseDouble(resposta.usdBrl().bid())).isGreaterThan(0.0);
        assertThat(Double.parseDouble(resposta.eurBrl().bid())).isGreaterThan(0.0);
    }
}