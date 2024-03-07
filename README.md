<h2 align="center">
Rinha backend 2024 - q1 - Kotlin
</h2>

<p align="center">Project created for the second edition of the backend rinha.</p>

## Requirements

- [JDK 17](https://sdkman.io/)
- [Kotlin 1.9.22](https://sdkman.io/)

## Technologies/Frameworks

- [Gradle](https://gradle.org/)
- [Ktor](https://ktor.io/)
- [Exposed](https://github.com/JetBrains/Exposed)

## Start

To start the application, execute a docker compose command.

```bash
docker compose up -d
```

## Endpoints

### REST

- ***POST*** /clientes/{id}/transacoes

<details>
 <summary><b>Request</b></summary>

```json
{
  "valor": 1000,
  "tipo" : "c",
  "descricao" : "descricao"
}
```
</details>

<details>
 <summary><b>Response</b></summary>

```json
{
  "limite" : 100000,
  "saldo" : -9098
}
```
</details>

- ***GET*** /clientes/{id}/transacoes

<details>
 <summary><b>Response</b></summary>

```json
{
  "saldo": {
    "total": -9098,
    "data_extrato": "2024-01-17T02:34:41.217753Z",
    "limite": 100000
  },
  "ultimas_transacoes": [
    {
      "valor": 10,
      "tipo": "c",
      "descricao": "descricao",
      "realizada_em": "2024-01-17T02:34:38.543030Z"
    },
    {
      "valor": 90000,
      "tipo": "d",
      "descricao": "descricao",
      "realizada_em": "2024-01-17T02:34:38.543030Z"
    }
  ]
}
```
</details>