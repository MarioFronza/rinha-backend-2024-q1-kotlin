package com.github.rinha.controllers.statement

import com.github.rinha.controllers.statement.ClientReportResponse.Companion.fromClientEntity
import com.github.rinha.controllers.statement.StatementTransactionResponse.Companion.fromTransactionEntity
import com.github.rinha.entity.Client
import com.github.rinha.entity.Transaction
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class StatementResponse(
    val saldo: ClientReportResponse,
    val ultimas_transacoes: List<StatementTransactionResponse>
) {

    companion object {
        fun fromClientAndTransactions(
            client: Client,
            transactions: List<Transaction>,
            statementDate: Instant
        ) = StatementResponse(
            saldo = fromClientEntity(client, statementDate),
            ultimas_transacoes = transactions.map { fromTransactionEntity(it) }
        )
    }
}