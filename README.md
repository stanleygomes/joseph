# Joseph 🧑‍💻

Projeto Spring Boot + Kotlin

Este projeto é um exemplo básico de aplicação Spring Boot escrita em Kotlin, configurada para rodar com Java 21.

## Sobre o projeto

Joseph é uma aplicação para cuidar de finanças pessoais, com foco inicial em ações (bolsa de valores). O objetivo é ajudar no controle, análise e acompanhamento de investimentos.

### Motivação do nome

O nome "Joseph" faz referência a José do Egito, personagem bíblico conhecido por sua sabedoria em administrar recursos e planejar para o futuro. Assim como José ajudou o Egito a se preparar para tempos de abundância e escassez, esta aplicação busca auxiliar no planejamento e gestão financeira.

## Arquitetura

O projeto adota a **Vertical Slice Architecture** (Arquitetura de Fatia Vertical).

Nesta abordagem, em vez de organizar o código por camadas técnicas (ex: `controllers`, `services`, `repositories`), nós o agrupamos por funcionalidade ou *feature*. Cada funcionalidade é uma "fatia" vertical que contém toda a lógica necessária, desde a API até o banco de dados. Por exemplo, toda a lógica relacionada ao cadastro de Ações (`Stock`) está contida no pacote `com.nazarethlabs.joseph.stock`.

Dentro de cada fatia, o código é organizado em 3 camadas principais:

- **Presentation (Apresentação):** Contém os `Controllers` que expõem a funcionalidade via API REST.
- **Application (Aplicação):** Contém os `Services` e `DTOs`, orquestrando a lógica de negócio da fatia.
- **Infrastructure (Infraestrutura):** Contém as `Entities` e `Repositories` que lidam com a persistência de dados.

Essa estrutura promove alta coesão e baixo acoplamento entre as funcionalidades, facilitando a manutenção e a evolução do sistema.

## Requisitos

- Java 21 ☕
- Gradle
- Podman e Podman Compose

## Database Migrations com Flyway

O projeto utiliza o Flyway para gerenciar a evolução do esquema do banco de dados. O Spring Boot está configurado para executar automaticamente as migrações pendentes sempre que a aplicação é iniciada.

### Como criar uma nova migração

Para adicionar uma nova alteração ao banco de dados, siga estes passos:

1.  Crie um novo arquivo SQL no diretório: `src/main/resources/db/migration`.
2.  O nome do arquivo **deve** seguir o padrão de nomenclatura do Flyway: `V<VERSAO>__<DESCRICAO_CURTA_EM_SNAKE_CASE>.sql`.

    -   `V<VERSAO>`: Começa com `V`, seguido pelo número da versão (ex: `V1`, `V2`, `V1.1`).
    -   `__`: Dois underscores separam a versão da descrição.
    -   `<DESCRICAO_CURTA_EM_SNAKE_CASE>.sql`: Uma breve descrição do que a migração faz, usando letras minúsculas e underscores no lugar de espaços.

**Exemplo prático (baseado na migração existente):**

-   **Nome do arquivo:** `V1__create_stock_table.sql`
-   **Conteúdo:**

    ```sql
    CREATE TABLE stocks (
        id UUID PRIMARY KEY,
        ticker VARCHAR(255) NOT NULL,
        company_name VARCHAR(255) NOT NULL,
        CONSTRAINT uk_ticker UNIQUE (ticker)
    );
    ```

Ao iniciar a aplicação, o Flyway detectará este novo arquivo, executará o script no banco de dados e registrará a migração na sua tabela de controle de esquema.

## Como Executar

### 1. Iniciar o Banco de Dados

O projeto utiliza Podman Compose para gerenciar o container do banco de dados PostgreSQL, conforme definido no arquivo `podman-compose.yml`.

Para iniciar o banco de dados em background, execute na raiz do projeto:

```sh
podman-compose up -d
```

Para parar e remover o container, execute:

```sh
podman-compose down
```

### 2. Executar a Aplicação

Com o banco de dados em execução, você pode rodar a aplicação Spring Boot:

```sh
./gradlew bootRun
```

Para rodar em modo debug e conectar um depurador na porta `5005`:

```sh
./gradlew bootRun --debug-jvm
```

## Scripts úteis

- `./gradlew build` — Compila o projeto
- `./gradlew test` — Executa os testes

