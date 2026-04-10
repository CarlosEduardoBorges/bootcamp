package com.financli.service;

import com.financli.model.Categoria;
import com.financli.model.Despesa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class DespesaServiceTest {

    @Autowired
    private DespesaService service;

    @Test
    void deveAdicionarDespesaValida() {
        Despesa salva = service.adicionarDespesa(
                "Almoço",
                new BigDecimal("25.00"),
                Categoria.ALIMENTACAO
        );

        assertNotNull(salva.getId());
        assertEquals(0, new BigDecimal("25.00").compareTo(salva.getValor()));
        assertEquals(Categoria.ALIMENTACAO, salva.getCategoria());
        assertEquals("Almoço", salva.getDescricao());
    }

    @Test
    void deveCalcularTotalGeral() {
        service.adicionarDespesa("Item 1", new BigDecimal("10.00"), Categoria.ALIMENTACAO);
        service.adicionarDespesa("Item 2", new BigDecimal("20.00"), Categoria.TRANSPORTE);
        service.adicionarDespesa("Item 3", new BigDecimal("30.00"), Categoria.LAZER);

        BigDecimal total = service.calcularTotalGeral();

        assertEquals(0, new BigDecimal("60.00").compareTo(total));
    }

    @Test
    void deveFiltrarPorCategoria() {
        service.adicionarDespesa("Almoço", new BigDecimal("15.00"), Categoria.ALIMENTACAO);
        service.adicionarDespesa("Jantar", new BigDecimal("20.00"), Categoria.ALIMENTACAO);
        service.adicionarDespesa("Ônibus", new BigDecimal("5.00"), Categoria.TRANSPORTE);

        List<Despesa> resultado = service.listarPorCategoria(Categoria.ALIMENTACAO);

        assertEquals(2, resultado.size());
        resultado.forEach(d -> assertEquals(Categoria.ALIMENTACAO, d.getCategoria()));
    }

    @Test
    void deveRejeitarValorNegativo() {
        assertThrows(IllegalArgumentException.class, () ->
                service.adicionarDespesa("Teste", new BigDecimal("-1.00"), Categoria.OUTROS)
        );
    }

    @Test
    void deveRejeitarDescricaoVazia() {
        assertThrows(IllegalArgumentException.class, () ->
                service.adicionarDespesa("", new BigDecimal("10.00"), Categoria.OUTROS)
        );

        assertThrows(IllegalArgumentException.class, () ->
                service.adicionarDespesa("   ", new BigDecimal("10.00"), Categoria.OUTROS)
        );
    }

    @Test
    void deveRetornarFalseAoRemoverIdInexistente() {
        boolean resultado = service.removerDespesa(9999L);

        assertFalse(resultado);
    }

    @Test
    void deveTotalZeroQuandoSemDespesas() {
        BigDecimal total = service.calcularTotalGeral();

        assertEquals(0, BigDecimal.ZERO.compareTo(total));
    }
}