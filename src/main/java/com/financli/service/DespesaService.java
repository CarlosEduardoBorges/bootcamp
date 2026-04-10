package com.financli.service;

import com.financli.model.Categoria;
import com.financli.model.Despesa;
import com.financli.repository.DespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DespesaService {

    private final DespesaRepository repository;

    @Autowired
    public DespesaService(DespesaRepository repository) {
        this.repository = repository;
    }

    public Despesa adicionarDespesa(String descricao, BigDecimal valor, Categoria categoria) {

        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("A descrição não pode ser vazia.");
        }

        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor deve ser maior que zero.");
        }

        Despesa d = new Despesa();
        d.setDescricao(descricao.trim());
        d.setValor(valor);
        d.setCategoria(categoria);
        d.setData(LocalDate.now());

        return repository.save(d);
    }

    public List<Despesa> listarTodas() {
        return repository.findAll();
    }

    public List<Despesa> listarPorCategoria(Categoria categoria) {

        if (categoria == null) {
            throw new IllegalArgumentException("Categoria inválida");
        }

        return repository.findByCategoria(categoria);
    }

    public BigDecimal calcularTotalGeral() {
        List<Despesa> todas = repository.findAll();
        return todas.stream().map(Despesa::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<Categoria, BigDecimal> calcularTotalPorCategoria() {
        List<Despesa> todas = repository.findAll();

        return todas.stream().collect(Collectors.groupingBy(Despesa::getCategoria, Collectors.reducing(BigDecimal.ZERO, Despesa::getValor, BigDecimal::add)));
    }

    public boolean removerDespesa(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }
}
