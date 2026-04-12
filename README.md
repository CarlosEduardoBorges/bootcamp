# FINANCLI

> CONTROLE DE DESPESAS PESSOAIS VIA LINHA DE COMANDO — SIMPLES, RÁPIDO E SEM DEPENDENCIAS EXTERNAS.

[![CI](https://github.com/CarlosEduardoBorges/bootcamp/actions/workflows/ci.yml/badge.svg)](https://github.com/CarlosEduardoBorges/bootcamp/actions/workflows/ci.yml)
![Version](https://img.shields.io/badge/version-1.0.0-blue)
![Java](https://img.shields.io/badge/Java-21-orange)
![License](https://img.shields.io/badge/license-MIT-green)

---

## INDICE

- [O Problema Real](#o-problema-real)
- [A Solução](#a-solução)
- [Público-alvo](#público-alvo)
- [Funcionalidades](#funcionalidades)
- [Tecnologias](#tecnologias)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Como Executar](#como-executar)
- [Como Rodar os Testes](#como-rodar-os-testes)
- [Como Rodar o Lint](#como-rodar-o-lint)
- [Exemplo de Uso](#exemplo-de-uso)
- [Versão](#versão)
- [Autor](#autor)

---

## O PROBLEMA REAL

A falta de controle sobre gastos cotidianos é um dos principais fatores que levam ao desequilíbrio financeiro, especialmente entre jovens e adultos. Aplicativos bancários costumam ser complexos demais para quem está começando, e anotações manuais são facilmente esquecidas ou perdidas. Segundo dados do Serasa, mais de 70 milhões de brasileiros estão inadimplentes — e a ausência de hábitos simples de controle financeiro é uma das causas mais recorrentes apontadas por especialistas.

Sem nenhum tipo de ferramenta de controle de gastos, é fácil:

- Perder a noção de quanto foi gasto no mês e em quais categorias;
- Comprometer a renda com despesas desnecessárias ou duplicadas;
- Chegar ao fim do mês sem saldo, sem entender onde o dinheiro foi parar;
- Acumular dívidas por falta de visibilidade sobre os próprios hábitos de consumo.

---

## A SOLUÇÃO

O **FinanCLI** é uma aplicação de linha de comando desenvolvida para tornar o controle financeiro pessoal simples e acessível. Principais características:

- Permite registrar despesas com descrição, valor, categoria e data de forma rápida pelo terminal;
- Exibe todas as despesas em tabelas formatadas e coloridas, facilitando a leitura;
- Oferece filtragem por categoria para identificar onde mais se gasta;
- Gera um resumo financeiro com total geral e subtotal por categoria;
- Persiste os dados localmente via banco H2 em arquivo, mantendo o histórico entre sessões;
- Não exige internet, cadastro ou infraestrutura externa — basta ter Java instalado.

---

## PÚBLICO-ALVO

Jovens e adultos que desejam iniciar um controle financeiro simples, sem depender de aplicativos com alta curva de aprendizado ou que exijam dados bancários.

---

## FUNCIONALIDADES

| # | FUNCIONALIDADE | DESCRIÇÃO |
|---|---|---|
| 1 | Registrar despesa | Cadastra uma nova despesa com descrição, valor, categoria e data |
| 2 | Listar despesas | Exibe todas as despesas registradas em tabela formatada |
| 3 | Filtrar por categoria | Lista apenas despesas de uma categoria específica |
| 4 | Resumo financeiro | Exibe total geral e subtotal por categoria em tabela |
| 5 | Remover despesa | Remove uma despesa pelo ID |
| 0 | Sair | Encerra a aplicação |

**CATEGORIAS DISPONÍVEIS:** `ALIMENTACAO`, `TRANSPORTE`, `LAZER`, `SAUDE`, `EDUCACAO`, `OUTROS`

---

## TECNOLOGIAS

| TECNOLOGIA | VERSÃO | USO |
|---|---|---|
| Java | 21 | Linguagem principal |
| Spring Boot | 3.5.13 | Framework e injeção de dependências |
| Spring Data JPA | — | Camada de persistência |
| H2 Database | — | Banco de dados embarcado (arquivo local) |
| JUnit 5 | — | Testes automatizados |
| Spring Boot Test | — | Contexto de testes com H2 em memória |
| Checkstyle | 3.3.1 | Análise estática / linting |
| Maven | 3.9.14 | Gerenciamento de build e dependências |
| GitHub Actions | — | Pipeline de CI |

---

## ESTRUTURA DO PROJETO

```
financli/
├── .github/
│   └── workflows/
│       └── ci.yml                        # Pipeline de CI
├── src/
│   ├── main/
│   │   ├── java/com/financli/
│   │   │   ├── FinancliApplication.java  # Ponto de entrada
│   │   │   ├── cli/
│   │   │   │   └── MenuCLI.java          # Interface de linha de comando
│   │   │   ├── model/
│   │   │   │   ├── Despesa.java          # Entidade JPA
│   │   │   │   └── Categoria.java        # Enum de categorias
│   │   │   ├── repository/
│   │   │   │   └── DespesaRepository.java
│   │   │   └── service/
│   │   │       └── DespesaService.java   # Regras de negócio
│   │   └── resources/
│   │       └── application.yaml
│   └── test/
│       ├── java/com/financli/
│       │   ├── FinancliApplicationTests.java
│       │   └── service/
│       │       └── DespesaServiceTest.java
│       └── resources/
│           └── application-test.yaml
├── checkstyle.xml
├── pom.xml
└── README.md
```

---

## PRÉ-REQUISITOS

```
- Java 21
- Maven 3.6 ou superior instalado (ou usar o `mvnw` incluso no projeto)
- Git (realizar os comandos git)
```

PARA VERIFICAR AS VERSÕES INSTALADAS:

```bash
java -version
mvn -version
```

---

## INSTALAÇÃO

```bash
# 1. CLONE O REPOSITÓRIO
git clone https://github.com/CarlosEduardoBorges/bootcamp.git

# 2. ACESSE A PASTA DO PROJETO
cd bootcamp

# 3. COMPILE O PROJETO
mvn compile
```

---

## COMO EXECUTAR

```bash
mvn spring-boot:run
```

O MENU INTERATIVO SERÁ EXIBIDO DIRETAMENTE NO TERMINAL:

```
========================================
   Bem-vindo ao FinanCLI v1.0.0
   Seu controle de gastos pessoais
========================================

+---------------------------------------+
|      FinanCLI - Menu Principal        |
+---------------------------------------+
|  1. Registrar despesa                 |
|  2. Listar todas as despesas          |
|  3. Filtrar por categoria             |
|  4. Ver resumo financeiro             |
|  5. Remover despesa                   |
|  0. Sair                              |
+---------------------------------------+
Escolha uma opção:
```

> OS DADOS SÃO SALVOS AUTOMATICAMENTE EM `./data/financli.mv.db` NA PASTA DO PROJETO. O ARQUIVO É CRIADO AUTOMATICAMENTE NA PRIMEIRA EXECUÇÃO.

---

## COMO RODAR OS TESTES

```bash
mvn test
```

A SUÍTE POSSUI **7 TESTES AUTOMATIZADOS** COBRINDO TRÊS CENÁRIOS:

| CENÁRIO | TESTES |
|---|---|
| Caminho feliz | Adicionar despesa válida, calcular total geral, filtrar por categoria, total zero sem despesas |
| Entrada inválida | Rejeitar valor negativo, rejeitar descrição vazia (incluindo espaços em branco) |
| Caso limite | Retornar `false` ao tentar remover ID inexistente |

OS TESTES UTILIZAM H2 EM MEMÓRIA COM PERFIL `test`, GARANTINDO ISOLAMENTO TOTAL DO BANCO DE PRODUÇÃO.

---

## COMO RODAR O LINT

```bash
mvn checkstyle:check
```

O PROJETO UTILIZA **CHECKSTYLE 3.3.1** COM REGRAS PERSONALIZADAS DEFINIDAS EM `checkstyle.xml`. AS VERIFICAÇÕES INCLUEM: IMPORTS NÃO UTILIZADOS, STAR IMPORTS, USO OBRIGATÓRIO DE CHAVES EM BLOCOS, ESPAÇAMENTO AO REDOR DE OPERADORES, CONVENÇÕES DE NOMENCLATURA E TAMANHO MÁXIMO DE ARQUIVO.

PARA GERAR O RELATÓRIO HTML COMPLETO SEM INTERROMPER O BUILD:

```bash
mvn checkstyle:checkstyle
```

O RELATÓRIO SERÁ GERADO EM `target/site/checkstyle.html`.

---

## EXEMPLO DE USO

**REGISTRANDO UMA DESPESA:**

```
Escolha uma opção: 1

--- Registrar Nova Despesa ---
Descrição: Almoço no restaurante
Valor (ex: 29.90): 35.50
Categorias disponíveis:
  1. ALIMENTACAO
  2. TRANSPORTE
  3. LAZER
  4. SAUDE
  5. EDUCACAO
  6. OUTROS
Escolha o número da categoria: 1
Despesa registrada com sucesso! ID: 1
```

**Listando despesas em tabela:**

```
+----+-------------------------+----------+--------------+------------+
| ID | Descrição               | Valor    | Categoria    | Data       |
+----+-------------------------+----------+--------------+------------+
| 1  | Almoço no restaurante   | R$ 35,50 | ALIMENTACAO  | 2026-04-12 |
| 2  | Passagem de ônibus      | R$  4,50 | TRANSPORTE   | 2026-04-12 |
+----+-------------------------+----------+--------------+------------+
```

**Resumo financeiro:**

```
+----------------+----------+
| Categoria      | Total    |
+----------------+----------+
| ALIMENTACAO    | R$ 35,50 |
| TRANSPORTE     | R$  4,50 |
+----------------+----------+
| TOTAL GERAL    | R$ 40,00 |
+----------------+----------+
```

---

## VERSÃO

**1.0.0** — Versão inicial com funcionalidades CRUD completas, interface tabular colorida, 7 testes automatizados, Checkstyle e pipeline CI/CD via GitHub Actions.

O versionamento segue o padrão **MAJOR.MINOR.PATCH** (Semantic Versioning). A versão está declarada no `pom.xml`:

```xml
<version>1.0.0</version>
```

---

## AUTOR

**Carlos Eduardo Rodrigues Borges Gonçalves**

- GitHub: [@CarlosEduardoBorges](https://github.com/CarlosEduardoBorges)
- Repositório: [https://github.com/CarlosEduardoBorges/bootcamp](https://github.com/CarlosEduardoBorges/bootcamp)