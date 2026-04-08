package com.financli.cli;

import com.financli.model.Categoria;
import com.financli.model.Despesa;
import com.financli.service.DespesaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

@Component
public class MenuCLI implements CommandLineRunner {

    private final DespesaService service;
    private final Scanner scanner = new Scanner(System.in);
    private final NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public MenuCLI(DespesaService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) {
        exibirBoasVindas();
        while (true) {
            exibirMenu();
            String opcao = scanner.nextLine().trim();
            switch (opcao) {
                case "1" -> registrarDespesa();
                case "2" -> listarTodas();
                case "3" -> filtrarPorCategoria();
                case "4" -> exibirResumo();
                case "5" -> removerDespesa();
                case "0" -> {
                    System.out.println("\nAté logo! Cuide bem do seu dinheiro.");
                    scanner.close();
                    return;
                }
                default -> System.out.println("\nOpção inválida. Digite um número de 0 a 5.");
            }
        }
    }

    private void exibirBoasVindas() {
        System.out.println("========================================");
        System.out.println("       FinanCLI — Controle de Gastos    ");
        System.out.println("========================================");
    }

    private void exibirMenu() {
        System.out.println("\n--- Menu Principal ---");
        System.out.println("1. Registrar despesa");
        System.out.println("2. Listar todas as despesas");
        System.out.println("3. Filtrar por categoria");
        System.out.println("4. Ver resumo financeiro");
        System.out.println("5. Remover despesa");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private void registrarDespesa() {
        System.out.println("\n--- Registrar Nova Despesa ---");

        System.out.print("Descrição: ");
        String descricao = scanner.nextLine().trim();

        System.out.print("Valor (ex: 29.90): ");
        BigDecimal valor;
        try {
            valor = new BigDecimal(scanner.nextLine().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido. Use apenas números, ex: 29.90");
            return;
        }

        Categoria categoria = lerCategoria();
        if (categoria == null) return;

        try {
            Despesa salva = service.adicionarDespesa(descricao, valor, categoria);
            System.out.println("Despesa registrada com sucesso! ID: " + salva.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listarTodas() {
        List<Despesa> lista = service.listarTodas();
        System.out.println("\n--- Todas as Despesas ---");
        if (lista.isEmpty()) {
            System.out.println("Nenhuma despesa registrada ainda.");
            return;
        }
        lista.forEach(d -> System.out.println(formatarDespesa(d)));
        System.out.println("Total: " + lista.size() + " despesa(s).");
    }

    private void filtrarPorCategoria() {
        System.out.println("\n--- Filtrar por Categoria ---");
        Categoria categoria = lerCategoria();
        if (categoria == null) return;

        List<Despesa> lista = service.listarPorCategoria(categoria);
        if (lista.isEmpty()) {
            System.out.println("Nenhuma despesa encontrada para: " + categoria);
            return;
        }
        lista.forEach(d -> System.out.println(formatarDespesa(d)));
    }

    private void exibirResumo() {
        System.out.println("\n--- Resumo Financeiro ---");

        BigDecimal total = service.calcularTotalGeral();
        System.out.println("Total geral: " + moeda.format(total));

        Map<Categoria, BigDecimal> porCategoria = service.calcularTotalPorCategoria();
        if (porCategoria.isEmpty()) {
            System.out.println("Sem despesas registradas.");
            return;
        }
        System.out.println("\nPor categoria:");
        porCategoria.forEach((cat, val) ->
                System.out.printf("  %-15s %s%n", cat, moeda.format(val))
        );
    }

    private void removerDespesa() {
        System.out.println("\n--- Remover Despesa ---");
        listarTodas();

        System.out.print("\nDigite o ID da despesa a remover: ");
        long id;
        try {
            id = Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Digite apenas números.");
            return;
        }

        boolean removida = service.removerDespesa(id);
        if (removida) {
            System.out.println("Despesa removida com sucesso.");
        } else {
            System.out.println("ID não encontrado. Nenhuma despesa foi removida.");
        }
    }

    private Categoria lerCategoria() {
        Categoria[] categorias = Categoria.values();
        System.out.println("Categorias disponíveis:");
        for (int i = 0; i < categorias.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, categorias[i]);
        }
        System.out.print("Escolha o número da categoria: ");

        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= categorias.length) {
                System.out.println("Opção fora do intervalo válido.");
                return null;
            }
            return categorias[idx];
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Digite o número da categoria.");
            return null;
        }
    }

    private String formatarDespesa(Despesa d) {
        return String.format("[%d] %-25s %s  %-15s %s",
                d.getId(),
                d.getDescricao(),
                moeda.format(d.getValor()),
                d.getCategoria(),
                d.getData()
        );
    }
}
