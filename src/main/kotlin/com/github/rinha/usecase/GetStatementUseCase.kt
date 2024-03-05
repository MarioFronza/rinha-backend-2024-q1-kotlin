package com.github.rinha.usecase

import com.github.rinha.controllers.statement.StatementResponse
import com.github.rinha.controllers.statement.StatementResponse.Companion.fromClientAndTransactions
import com.github.rinha.persistence.client.ClientRepository
import com.github.rinha.persistence.transaction.TransactionRepository
import java.time.Instant

class GetStatementUseCase(
    val clientRepository: ClientRepository,
    val transactionRepository: TransactionRepository
) {

    suspend fun getBy(clientId: Int): StatementResponse {
        val client = clientRepository.findById(clientId) ?: throw Exception("not found")
        val transactions = transactionRepository.findByClientId(clientId)
        return fromClientAndTransactions(client, transactions, statementDate = Instant.now())
    }

}