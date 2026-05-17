# Changelog

Todas as mudanças notáveis neste projeto serão documentadas neste arquivo.

O formato segue o padrão [Keep a Changelog](https://keepachangelog.com/pt-BR/1.0.0/),
e este projeto adota o [Versionamento Semântico](https://semver.org/lang/pt-BR/).

---

## [1.1.0] - 2026-05-17

### Adicionado
- Integração com a AwesomeAPI para consulta de cotações USD-BRL e EUR-BRL (opção 6 do menu)
- Teste de integração com WireMock para validar o fluxo de consumo da API
- Containerização via Docker (multi-stage build) para deploy em nuvem
- Endpoint `/actuator/health` para health check do serviço deployado
- Profile `cloud` para execução headless em container (sem CLI interativo)

### Modificado
- URL da API de cotações extraída para `application.yaml` (`cotacao.api.url`), permitindo override em testes
- Trigger do GitHub Actions ampliado para incluir a branch `entrega-intermediaria`
- `open-in-view` desativado no JPA para suprimir warning desnecessário

### Tecnologias
- Java 21, Spring Boot 3.5.13, Spring Web, Spring Actuator, WireMock 3.9.1, Docker, Render.com

---

## [1.0.0] - 2026-04-12

### Adicionado
- Interface CLI interativa com menu numerado (`MenuCLI.java`)
- Entidade `Despesa` com campos: `id`, `descricao`, `valor`, `categoria`, `data`
- Enum `Categoria` com valores: `ALIMENTACAO`, `TRANSPORTE`, `LAZER`, `SAUDE`, `EDUCACAO`, `OUTROS`
- `DespesaRepository` com Spring Data JPA para persistência automática
- `DespesaService` com regras de negócio: validação de valor negativo e descrição vazia
- Funcionalidade de registrar nova despesa (opção 1)
- Funcionalidade de listar todas as despesas em tabela formatada (opção 2)
- Funcionalidade de filtrar despesas por categoria (opção 3)
- Funcionalidade de resumo financeiro com total por categoria e total geral (opção 4)
- Funcionalidade de remover despesa por ID (opção 5)
- Saída colorida no terminal via códigos ANSI
- Persistência via H2 em arquivo local (`./data/financli.mv.db`)
- 7 testes automatizados com JUnit 5 e Spring Boot Test (H2 em memória)
- Análise estática configurada com Checkstyle 3.3.1 e regras personalizadas (`checkstyle.xml`)
- Pipeline de CI com GitHub Actions executando lint e testes em cada push e pull request
- Badge de build do GitHub Actions no README
- Documentação completa no `README.md`

### Tecnologias
- Java 21, Spring Boot 3.5.13, Spring Data JPA, H2, JUnit 5, Checkstyle, Maven

---

[1.1.0]: https://github.com/CarlosEduardoBorges/bootcamp/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/CarlosEduardoBorges/bootcamp/releases/tag/v1.0.0