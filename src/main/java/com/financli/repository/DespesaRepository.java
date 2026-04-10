package com.financli.repository;

import com.financli.model.Categoria;
import com.financli.model.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {
    List<Despesa> findByCategoria(Categoria categoria);
}
