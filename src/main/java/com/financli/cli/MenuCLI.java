package com.financli.cli;

import com.financli.model.Categoria;
import com.financli.model.CotacaoResponse;
import com.financli.model.Despesa;
import com.financli.service.CotacaoService;
import com.financli.service.DespesaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

@Component
@Profile("!test")
public class MenuCLI implements CommandLineRunner {

    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String BOLD = "\u001B[1m";
    private static final String COL_SEP = " | ";

    private final DespesaService service;
    private final CotacaoService cotacaoService;
    private final Scanner scanner = new Scanner(System.in);
    private final NumberFormat moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public MenuCLI(DespesaService service, CotacaoService cotacaoService) {
        this.service = service;
        this.cotacaoService = cotacaoService;
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
                case "6" -> exibirCotacoes();
                case "0" -> {
                    System.out.println("\n" + GREEN + "Até logo! Cuide bem do seu dinheiro." + RESET);
                    scanner.close();
                    return;
                }
                default -> System.out.println(RED + "\nOpção inválida. Digite um número de 0 a 6." + RESET);
            }
        }
    }

    private void exibirBoasVindas() {
        System.out.println(BOLD + "\n========================================" + RESET);
        System.out.println(BOLD + "   Bem-vindo ao FinanCLI v1.0.0" + RESET);
        System.out.println(BOLD + "   Seu controle de gastos pessoais" + RESET);
        System.out.println(BOLD + "========================================" + RESET);
    }

    private void exibirMenu() {
        System.out.println("\n+---------------------------------------+");
        System.out.println(BOLD + "|      FinanCLI - Menu Principal        |" + RESET);
        System.out.println("+---------------------------------------+");
        System.out.println("|  1. Registrar despesa                 |");
        System.out.println("|  2. Listar todas as despesas          |");
        System.out.println("|  3. Filtrar por categoria             |");
        System.out.println("|  4. Ver resumo financeiro             |");
        System.out.println("|  5. Remover despesa                   |");
        System.out.println("|  6. Ver cotações do dia               |");
        System.out.println("|  0. Sair                              |");
        System.out.println("+---------------------------------------+");
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
            System.out.println(RED + "Valor inválido. Use apenas números, ex: 29.90" + RESET);
            return;
        }

        Categoria categoria = lerCategoria();
        if (categoria == null) {
            return;
        }

        try {
            Despesa salva = service.adicionarDespesa(descricao, valor, categoria);
            System.out.println(GREEN + "Despesa registrada com sucesso! ID: " + salva.getId() + RESET);
        } catch (IllegalArgumentException e) {
            System.out.println(RED + "Erro: " + e.getMessage() + RESET);
        }
    }

    private void listarTodas() {
        List<Despesa> lista = service.listarTodas();
        System.out.println();
        if (lista.isEmpty()) {
            System.out.println(YELLOW + "Nenhuma despesa registrada ainda." + RESET);
            return;
        }
        imprimirTabelaDespesas(lista);
        System.out.println("Total: " + lista.size() + " despesa(s).");
    }

    private void filtrarPorCategoria() {
        System.out.println("\n--- Filtrar por Categoria ---");
        Categoria categoria = lerCategoria();
        if (categoria == null) {
            return;
        }
        List<Despesa> lista = service.listarPorCategoria(categoria);
        System.out.println();
        if (lista.isEmpty()) {
            System.out.println(YELLOW + "Nenhuma despesa encontrada para: " + categoria + RESET);
            return;
        }
        imprimirTabelaDespesas(lista);
    }

    private void exibirResumo() {
        System.out.println("\n--- Resumo Financeiro ---");
        BigDecimal total = service.calcularTotalGeral();
        Map<Categoria, BigDecimal> porCategoria = service.calcularTotalPorCategoria();

        if (porCategoria.isEmpty()) {
            System.out.println(YELLOW + "Sem despesas registradas." + RESET);
            return;
        }

        String hCat = "Categoria";
        String hVal = "Total";
        int wCat = Math.max(hCat.length(), porCategoria.keySet().stream()
                .mapToInt(c -> c.name().length()).max().orElse(10));
        int wVal = Math.max(hVal.length(), porCategoria.values().stream()
                .mapToInt(v -> moeda.format(v).length()).max().orElse(10));

        String linha = "+" + "-".repeat(wCat + 2) + "+" + "-".repeat(wVal + 2) + "+";
        System.out.println(linha);
        System.out.printf(BOLD + "| %-" + wCat + "s | %-" + wVal + "s |" + RESET + "%n",
                hCat, hVal);
        System.out.println(linha);

        porCategoria.forEach((cat, val) ->
                System.out.printf("| %-" + wCat + "s | %-" + wVal + "s |%n",
                        cat.name(), moeda.format(val))
        );

        System.out.println(linha);
        System.out.printf(BOLD + "| %-" + wCat + "s | %-" + wVal + "s |" + RESET + "%n",
                "TOTAL GERAL", moeda.format(total));
        System.out.println(linha);
    }

    private void removerDespesa() {
        System.out.println("\n--- Remover Despesa ---");
        listarTodas();

        System.out.print("\nDigite o ID da despesa a remover: ");
        long id;
        try {
            id = Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println(RED + "ID inválido. Digite apenas números." + RESET);
            return;
        }

        boolean removida = service.removerDespesa(id);
        if (removida) {
            System.out.println(GREEN + "Despesa removida com sucesso." + RESET);
        } else {
            System.out.println(RED + "ID não encontrado. Nenhuma despesa foi removida." + RESET);
        }
    }

    private void imprimirTabelaDespesas(List<Despesa> lista) {
        String hId = "ID";
        String hDesc = "Descrição";
        String hVal = "Valor";
        String hCat = "Categoria";
        String hData = "Data";

        int wId = Math.max(hId.length(),
                lista.stream().mapToInt(d -> d.getId().toString().length()).max().orElse(2));
        int wDesc = Math.max(hDesc.length(),
                lista.stream().mapToInt(d -> d.getDescricao().length()).max().orElse(10));
        int wVal = Math.max(hVal.length(),
                lista.stream().mapToInt(d -> moeda.format(d.getValor()).length()).max().orElse(10));
        int wCat = Math.max(hCat.length(),
                lista.stream().mapToInt(d -> d.getCategoria().name().length()).max().orElse(10));
        int wData = Math.max(hData.length(), 10);

        String linha = "+" + "-".repeat(wId + 2)
                + "+" + "-".repeat(wDesc + 2)
                + "+" + "-".repeat(wVal + 2)
                + "+" + "-".repeat(wCat + 2)
                + "+" + "-".repeat(wData + 2) + "+";

        System.out.println(linha);
        System.out.printf(BOLD + "| %-" + wId + "s | %-" + wDesc + "s | %-"
                        + wVal + "s | %-" + wCat + "s | %-" + wData + "s |" + RESET + "%n",
                hId, hDesc, hVal, hCat, hData);
        System.out.println(linha);

        for (Despesa d : lista) {
            System.out.printf("| %-" + wId + "s | %-" + wDesc + "s | %-"
                            + wVal + "s | %-" + wCat + "s | %-" + wData + "s |%n",
                    d.getId(),
                    d.getDescricao(),
                    moeda.format(d.getValor()),
                    d.getCategoria().name(),
                    d.getData());
        }
        System.out.println(linha);
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
                System.out.println(RED + "Opção fora do intervalo válido." + RESET);
                return null;
            }
            return categorias[idx];
        } catch (NumberFormatException e) {
            System.out.println(RED + "Entrada inválida. Digite o número da categoria." + RESET);
            return null;
        }
    }

    private void exibirCotacoes() {
        System.out.println("\n=== COTAÇÕES DO DIA ===");
        CotacaoResponse cotacoes = cotacaoService.buscarCotacoes();
        if (cotacoes == null) {
            System.out.println(YELLOW + "Não foi possível "
                + "obter cotações. Verifique sua conexão." + RESET);
            return;
        }
        if (cotacoes.usdBrl() != null) {
            System.out.printf("Dólar (USD): R$ %s%n",
                cotacoes.usdBrl().bid());
        }
        if (cotacoes.eurBrl() != null) {
            System.out.printf("Euro  (EUR): R$ %s%n",
                cotacoes.eurBrl().bid());
        }
    }
}
