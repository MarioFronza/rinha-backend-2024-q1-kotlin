package com.github.rinha.usecase.statement.models

import com.github.rinha.usecase.statement.models.ClientReportOutput.Companion.fromClientEntity
import com.github.rinha.usecase.statement.models.StatementTransactionOutput.Companion.fromTransactionEntity
import com.github.rinha.entity.Client
import com.github.rinha.entity.Transaction
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class StatementOutput(
    val saldo: ClientReportOutput,
    val ultimas_transacoes: List<StatementTransactionOutput>
) {

    companion object {
        fun fromClientAndTransactions(
            client: Client,
            transactions: List<Transaction>,
            statementDate: Instant
        ) = StatementOutput(
            saldo = fromClientEntity(client, statementDate),
            ultimas_transacoes = transactions.map { fromTransactionEntity(it) }
        )
    }
}