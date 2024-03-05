package com.github.rinha.plugins

import com.github.rinha.persistence.client.ExposedClientRepository
import com.github.rinha.persistence.transaction.ExposedTransactionRepository
import com.github.rinha.usecase.statement.GetStatementUseCase
import com.github.rinha.usecase.transaction.CreateTransactionUseCase
import com.github.rinha.usecase.transaction.models.CreateTransactionInput
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    val clientRepository = ExposedClientRepository()
    val transactionRepository = ExposedTransactionRepository()

    val createTransactionUseCase = CreateTransactionUseCase(
        clientRepository,
        transactionRepository
    )

    val getStatementUseCase = GetStatementUseCase(
        clientRepository,
        transactionRepository
    )

    install(ContentNegotiation) {
        json()
    }

    routing {
        route("/clientes") {
            post("/{id}/transacoes") {
                val id = call.parameters["id"]?.toInt() ?: throw Exception()
                val input = call.receive<CreateTransactionInput>()
                val response = createTransactionUseCase.create(id, input)
                call.respond(response)
            }

            get("/{id}/extrato") {
                val id = call.parameters["id"]?.toInt() ?: throw Exception()
                val response = getStatementUseCase.getBy(id)
                call.respond(response)
            }
        }
    }

}