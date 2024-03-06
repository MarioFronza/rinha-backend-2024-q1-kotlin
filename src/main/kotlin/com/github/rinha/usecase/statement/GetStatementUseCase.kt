package com.github.rinha.usecase.statement

import com.github.rinha.usecase.statement.models.StatementOutput.Companion.fromClientAndTransactions
import com.github.rinha.persistence.client.ClientRepository
import com.github.rinha.persistence.transaction.TransactionRepository
import com.github.rinha.usecase.notification.NotificationOutput
import com.github.rinha.usecase.notification.NotificationOutput.NotificationError
import com.github.rinha.usecase.notification.NotificationOutput.NotificationSuccess
import com.github.rinha.usecase.statement.models.StatementOutput
import io.ktor.http.*
import java.time.Instant

class GetStatementUseCase(
    private val clientRepository: ClientRepository,
    private val transactionRepository: TransactionRepository
) {

    suspend fun getBy(clientId: Int): NotificationOutput<StatementOutput> {
        val client = clientRepository.findById(clientId) ?: return NotificationError(
            message = "Client with id $clientId was not found",
            type = HttpStatusCode.NotFound
        )
        val transactions = transactionRepository.findByClientId(clientId)
        return NotificationSuccess(
            data = fromClientAndTransactions(
                client = client,
                transactions = transactions,
                statementDate = Instant.now()
            )
        )
    }

}