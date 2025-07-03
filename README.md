# Joseph 🧑‍💻

Projeto Spring Boot + Kotlin

Este projeto é um exemplo básico de aplicação Spring Boot escrita em Kotlin, configurada para rodar com Java 21.

## Sobre o projeto

Joseph é uma aplicação para cuidar de finanças pessoais, com foco inicial em ações (bolsa de valores). O objetivo é ajudar no controle, análise e acompanhamento de investimentos.

### Motivação do nome

O nome "Joseph" faz referência a José do Egito, personagem bíblico conhecido por sua sabedoria em administrar recursos e planejar para o futuro. Assim como José ajudou o Egito a se preparar para tempos de abundância e escassez, esta aplicação busca auxiliar no planejamento e gestão financeira.

## Requisitos

- Java 21 ☕
- Gradle
- Podman e Podman Compose
- VS Code (com extensões Java e Kotlin) (Opcional)

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

---

Feito com ❤️ usando Spring Boot e Kotlin.
