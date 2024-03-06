package com.github.rinha.plugins

import com.github.rinha.entity.notification.NotificationOutput.NotificationError
import com.github.rinha.entity.notification.NotificationOutput.NotificationSuccess
import com.github.rinha.persistence.client.ExposedClientRepository
import com.github.rinha.persistence.transaction.ExposedTransactionRepository
import com.github.rinha.plugins.utils.requiredIntParameter
import com.github.rinha.plugins.utils.toHttpStatus
import com.github.rinha.usecase.statement.GetStatementUseCase
import com.github.rinha.usecase.statement.models.StatementOutput
import com.github.rinha.usecase.transaction.CreateTransactionUseCase
import com.github.rinha.usecase.transaction.models.CreateTransactionInput
import com.github.rinha.usecase.transaction.models.TransactionOutput
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.NotFound
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
                val id = requiredIntParameter("id", NotFound) ?: return@post
                val input = try {
                    call.receive<CreateTransactionInput>()
                } catch (ex: Exception) {
                    call.respond(BadRequest)
                    return@post
                }
                when (val response = createTransactionUseCase.create(id, input)) {
                    is NotificationSuccess<TransactionOutput> -> call.respond(response.data)
                    is NotificationError -> call.respond(response.type.toHttpStatus(), response.message)
                }
            }

            get("/{id}/extrato") {
                val id = requiredIntParameter("id", NotFound) ?: return@get
                when (val response = getStatementUseCase.getBy(id)) {
                    is NotificationSuccess<StatementOutput> -> call.respond(response.data)
                    is NotificationError -> call.respond(response.type.toHttpStatus(), response.message)
                }
            }
        }
    }

}

